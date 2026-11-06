// Carga los datos de la vacuna en el modal Agregar o Editar
document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('vacunaModal');
    if (!modal) {
        return;
    }
    const form = document.getElementById('vacunaForm');
    const header = document.getElementById('vacunaModalHeader');
    const titulo = document.getElementById('vacunaModalTitulo');
    const id = document.getElementById('vacuna-id');
    const expediente = document.getElementById('vacuna-expediente');
    const nombre = document.getElementById('vacuna-nombre');
    const aplicacion = document.getElementById('vacuna-aplicacion');
    const proxima = document.getElementById('vacuna-proxima');
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
            expediente.value = button.getAttribute('data-expediente') || '';
            nombre.value = button.getAttribute('data-nombre') || '';
            aplicacion.value = button.getAttribute('data-aplicacion') || '';
            proxima.value = button.getAttribute('data-proxima') || '';
            header.classList.add('bg-warning');
        } else {
            header.classList.add('bg-info', 'text-white');
        }
    });
});