// Carga los datos de la cita en el modal Agendar o Reprogramar
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
    const hoy = modal.getAttribute('data-hoy');
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
        // No se permiten fechas que ya pasaron
        fecha.min = hoy;
        if (modo === 'editar') {
            // Al reprogramar solo cambian el veterinario, la fecha y la hora
            id.value = button.getAttribute('data-id') || '';
            mascota.value = button.getAttribute('data-mascota') || '';
            servicio.value = button.getAttribute('data-servicio') || '';
            veterinario.value = button.getAttribute('data-veterinario') || '';
            fecha.value = button.getAttribute('data-fecha') || '';
            hora.value = button.getAttribute('data-hora') || '';
            motivo.value = button.getAttribute('data-motivo') || '';
            mascota.disabled = true;
            servicio.disabled = true;
            motivo.disabled = true;
            header.classList.add('bg-warning');
        } else {
            // Una cita nueva: todo editable
            mascota.disabled = false;
            servicio.disabled = false;
            motivo.disabled = false;
            header.classList.add('bg-info', 'text-white');
        }
    });
});