// Carga los datos del veterinario en el modal Agregar o Editar
document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('veterinarioModal');
    if (!modal) {
        return;
    }
    const form = document.getElementById('veterinarioForm');
    const header = document.getElementById('veterinarioModalHeader');
    const titulo = document.getElementById('veterinarioModalTitulo');
    const id = document.getElementById('veterinario-id');
    const identificacion = document.getElementById('veterinario-identificacion');
    const nombre = document.getElementById('veterinario-nombre');
    const especialidad = document.getElementById('veterinario-especialidad');
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
            identificacion.value = button.getAttribute('data-identificacion') || '';
            nombre.value = button.getAttribute('data-nombre') || '';
            especialidad.value = button.getAttribute('data-especialidad') || '';
            header.classList.add('bg-warning');
        } else {
            header.classList.add('bg-info', 'text-white');
        }
    });
});