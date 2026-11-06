// Carga los datos del servicio en el modal Agregar o Editar
document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('servicioModal');
    if (!modal) {
        return;
    }
    const form = document.getElementById('servicioForm');
    const header = document.getElementById('servicioModalHeader');
    const titulo = document.getElementById('servicioModalTitulo');
    const id = document.getElementById('servicio-id');
    const nombre = document.getElementById('servicio-nombre');
    const descripcion = document.getElementById('servicio-descripcion');
    const precio = document.getElementById('servicio-precio');
    const estado = document.getElementById('servicio-estado');
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
            nombre.value = button.getAttribute('data-nombre') || '';
            descripcion.value = button.getAttribute('data-descripcion') || '';
            precio.value = button.getAttribute('data-precio') || '';
            estado.checked = button.getAttribute('data-estado') === 'true';
            header.classList.add('bg-warning');
        } else {
            // Un servicio nuevo inicia activo
            estado.checked = true;
            header.classList.add('bg-info', 'text-white');
        }
    });
});