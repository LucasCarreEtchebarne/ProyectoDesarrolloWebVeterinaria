// Carga los datos de la mascota en el modal Agregar o Editar
document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('mascotaModal');
    if (!modal) {
        return;
    }
    const form = document.getElementById('mascotaForm');
    const header = document.getElementById('mascotaModalHeader');
    const titulo = document.getElementById('mascotaModalTitulo');
    const id = document.getElementById('mascota-id');
    const cliente = document.getElementById('mascota-cliente');
    const nombre = document.getElementById('mascota-nombre');
    const especie = document.getElementById('mascota-especie');
    const raza = document.getElementById('mascota-raza');
    const fecha = document.getElementById('mascota-fecha');
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
            cliente.value = button.getAttribute('data-cliente') || '';
            nombre.value = button.getAttribute('data-nombre') || '';
            especie.value = button.getAttribute('data-especie') || '';
            raza.value = button.getAttribute('data-raza') || '';
            fecha.value = button.getAttribute('data-fecha') || '';
            header.classList.add('bg-warning');
        } else {
            header.classList.add('bg-info', 'text-white');
        }
    });
});