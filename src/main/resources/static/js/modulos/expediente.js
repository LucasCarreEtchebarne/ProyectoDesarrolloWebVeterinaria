// Carga los datos del expediente en el modal Agregar o Editar
document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('expedienteModal');
    if (!modal) {
        return;
    }
    const form = document.getElementById('expedienteForm');
    const header = document.getElementById('expedienteModalHeader');
    const titulo = document.getElementById('expedienteModalTitulo');
    const id = document.getElementById('expediente-id');
    const mascota = document.getElementById('expediente-mascota');
    const veterinario = document.getElementById('expediente-veterinario');
    const fecha = document.getElementById('expediente-fecha');
    const sintomas = document.getElementById('expediente-sintomas');
    const observaciones = document.getElementById('expediente-observaciones');
    const diagnostico = document.getElementById('expediente-diagnostico');
    const tratamiento = document.getElementById('expediente-tratamiento');
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
        if (modo === 'editar') {
            id.value = button.getAttribute('data-id') || '';
            mascota.value = button.getAttribute('data-mascota') || '';
            veterinario.value = button.getAttribute('data-veterinario') || '';
            fecha.value = button.getAttribute('data-fecha') || '';
            sintomas.value = button.getAttribute('data-sintomas') || '';
            observaciones.value = button.getAttribute('data-observaciones') || '';
            diagnostico.value = button.getAttribute('data-diagnostico') || '';
            tratamiento.value = button.getAttribute('data-tratamiento') || '';
            header.classList.add('bg-warning');
        } else {
            header.classList.add('bg-info', 'text-white');
        }
    });
});