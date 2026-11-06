//Para insertar información en el modal de eliminar según el registro...
document.addEventListener('DOMContentLoaded', function () {
    const confirmModal = document.getElementById('confirmModal');
    if (!confirmModal) {
        return;
    }
    confirmModal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        document.getElementById('modalId').value = button.getAttribute('data-bs-id');
        document.getElementById('modalDescripcion').textContent = button.getAttribute('data-bs-descripcion');
    });
});

//Carga los datos del registro en el modal de Agregar o Actualizar.
//Sirve para todos los modales con la clase "modal-formulario":
//cada atributo data-campo-xxx del botón se copia en el campo con id "xxx" del formulario.
document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.modal-formulario').forEach(function (modal) {
        modal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            if (!button) {
                return;
            }
            const form = modal.querySelector('form');
            const header = modal.querySelector('.modal-header');
            // Se limpia cualquier información de una apertura anterior
            form.reset();
            form.querySelectorAll('input[type="hidden"]').forEach(function (campo) {
                campo.value = '';
            });
            modal.querySelector('.modal-title').textContent = button.getAttribute('data-titulo') || '';
            header.classList.remove('bg-info', 'bg-warning', 'text-white');
            if (button.getAttribute('data-modo') === 'editar') {
                Array.from(button.attributes).forEach(function (atributo) {
                    if (atributo.name.startsWith('data-campo-')) {
                        const campo = form.querySelector('#' + atributo.name.substring(11));
                        if (campo) {
                            if (campo.type === 'checkbox') {
                                campo.checked = atributo.value === 'true';
                            } else {
                                campo.value = atributo.value;
                            }
                        }
                    }
                });
                header.classList.add('bg-warning');
            } else {
                // Un registro nuevo inicia activo
                form.querySelectorAll('input[type="checkbox"]').forEach(function (campo) {
                    campo.checked = true;
                });
                header.classList.add('bg-info', 'text-white');
            }
        });
    });
});

//Para quitar toast
document.addEventListener('DOMContentLoaded', function () {
    setTimeout(() => {
        document.querySelectorAll('.toast').forEach(toast =>
            toast.classList.remove('show'));
    }, 4000);
});