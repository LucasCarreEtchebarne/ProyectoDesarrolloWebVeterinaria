// Carga los datos del cliente en el modal Agregar o Editar
document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('clienteModal');
    if (!modal) {
        return;
    }
    const form = document.getElementById('clienteForm');
    const header = document.getElementById('clienteModalHeader');
    const titulo = document.getElementById('clienteModalTitulo');
    const id = document.getElementById('cliente-id');
    const identificacion = document.getElementById('cliente-identificacion');
    const nombre = document.getElementById('cliente-nombre');
    const telefono = document.getElementById('cliente-telefono');
    const correo = document.getElementById('cliente-correo');
    const direccion = document.getElementById('cliente-direccion');
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
            telefono.value = button.getAttribute('data-telefono') || '';
            correo.value = button.getAttribute('data-correo') || '';
            direccion.value = button.getAttribute('data-direccion') || '';
            header.classList.add('bg-warning');
        } else {
            header.classList.add('bg-info', 'text-white');
        }
    });
});