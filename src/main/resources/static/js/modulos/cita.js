// Carga los datos de la cita en el modal Agendar o Reprogramar y llena las horas disponibles
document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('citaModal');
    if (!modal) {
        return;
    }
    const form = document.getElementById('citaForm');
    const header = document.getElementById('citaModalHeader');
    const titulo = document.getElementById('citaModalTitulo');
    const id = document.getElementById('citaId');
    const mascota = document.getElementById('citaMascota');
    const servicio = document.getElementById('citaServicio');
    const veterinario = document.getElementById('citaVeterinario');
    const fecha = document.getElementById('citaFecha');
    const hora = document.getElementById('citaHora');
    const motivo = document.getElementById('citaMotivo');
    const url = hora.getAttribute('data-url');
    const hoy = modal.getAttribute('data-hoy');
 
    function ponerOpciones(lista) {
        hora.innerHTML = '';
        lista.forEach(function (o) {
            const opt = document.createElement('option');
            opt.value = o.valor;
            opt.textContent = o.texto;
            hora.appendChild(opt);
        });
    }
 
    // Pide al servidor las horas libres del veterinario en esa fecha.
    // horaActual (opcional): hora que se deja seleccionada al reprogramar.
    async function cargarHoras(horaActual) {
        if (!veterinario.value || !fecha.value) {
            ponerOpciones([{valor: '', texto: 'Seleccione veterinario y fecha'}]);
            return;
        }
        const params = new URLSearchParams({idVeterinario: veterinario.value, fecha: fecha.value});
        if (id.value) {
            params.append('idCita', id.value); // su propia hora cuenta como libre
        }
        try {
            const respuesta = await fetch(url + '?' + params.toString());
            if (!respuesta.ok) {
                throw new Error('Error ' + respuesta.status);
            }
            const horas = await respuesta.json();
            if (horas.length === 0) {
                ponerOpciones([{valor: '', texto: 'Sin horarios disponibles'}]);
                return;
            }
            const opciones = [{valor: '', texto: 'Seleccione una hora'}];
            horas.forEach(function (h) {
                opciones.push({valor: h, texto: h});
            });
            ponerOpciones(opciones);
            if (horaActual) {
                hora.value = horaActual.substring(0, 5);
            }
        } catch (e) {
            ponerOpciones([{valor: '', texto: 'No se pudieron cargar los horarios'}]);
        }
    }
 
    veterinario.addEventListener('change', function () { cargarHoras(); });
    fecha.addEventListener('change', function () { cargarHoras(); });
 
    modal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        if (!button) {
            return;
        }
        const modo = button.getAttribute('data-modo');
        // Se limpia cualquier información de una apertura anterior
        form.reset();
        id.value = '';
        titulo.textContent = button.getAttribute('data-titulo') || '';
        header.classList.remove('bg-info', 'bg-warning', 'text-white');
        fecha.min = hoy;
 
        if (modo === 'editar') {
            // Reprogramar: solo cambian veterinario, fecha y hora
            id.value = button.getAttribute('data-id') || '';
            mascota.value = button.getAttribute('data-mascota') || '';
            servicio.value = button.getAttribute('data-servicio') || '';
            veterinario.value = button.getAttribute('data-veterinario') || '';
            fecha.value = button.getAttribute('data-fecha') || '';
            motivo.value = button.getAttribute('data-motivo') || '';
            mascota.disabled = true;
            servicio.disabled = true;
            motivo.disabled = true;
            mascota.required = false;
            servicio.required = false;
            header.classList.add('bg-warning');
            cargarHoras(button.getAttribute('data-hora'));
        } else {
            // Una cita nueva: todo editable
            mascota.disabled = false;
            servicio.disabled = false;
            motivo.disabled = false;
            mascota.required = true;
            servicio.required = true;
            header.classList.add('bg-info', 'text-white');
            cargarHoras();
        }
    });
});