// Carga los datos de la factura en el modal Agregar o Editar
document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('facturaModal');
    if (!modal) {
        return;
    }
    const form = document.getElementById('facturaForm');
    const header = document.getElementById('facturaModalHeader');
    const titulo = document.getElementById('facturaModalTitulo');
    const id = document.getElementById('factura-id');
    const numero = document.getElementById('factura-numero');
    const cliente = document.getElementById('factura-cliente');
    const cita = document.getElementById('factura-cita');
    const fecha = document.getElementById('factura-fecha');
    const monto = document.getElementById('factura-monto');
    const metodo = document.getElementById('factura-metodo');
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
            numero.value = button.getAttribute('data-numero') || '';
            cliente.value = button.getAttribute('data-cliente') || '';
            cita.value = button.getAttribute('data-cita') || '';
            fecha.value = button.getAttribute('data-fecha') || '';
            monto.value = button.getAttribute('data-monto') || '';
            metodo.value = button.getAttribute('data-metodo') || '';
            header.classList.add('bg-warning');
        } else {
            header.classList.add('bg-info', 'text-white');
        }
    });
});