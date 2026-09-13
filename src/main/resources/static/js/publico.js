
document.addEventListener("DOMContentLoaded", () => {

    console.log("Portafolio público cargado correctamente 🚀");


    // =====================================================
    // ANIMACIÓN DE APARICIÓN AL HACER SCROLL
    // =====================================================

    const elementos = document.querySelectorAll(
        ".about-card, .week-card, .contact-box"
    );

    const observer = new IntersectionObserver(
        (entradas) => {

            entradas.forEach((entrada) => {

                if (entrada.isIntersecting) {

                    entrada.target.style.opacity = "1";

                    entrada.target.style.transform =
                        "translateY(0)";

                }

            });

        },
        {
            threshold: 0.15
        }
    );


    elementos.forEach((elemento) => {

        elemento.style.opacity = "0";

        elemento.style.transform =
            "translateY(40px)";

        elemento.style.transition =
            "all 0.7s ease";

        observer.observe(elemento);

    });


    // =====================================================
    // ELEMENTOS DEL MODAL
    // =====================================================

    const btnAbrirMensaje =
        document.getElementById("btnAbrirMensaje");

    const btnCerrarMensaje =
        document.getElementById("btnCerrarMensaje");

    const btnCancelarMensaje =
        document.getElementById("btnCancelarMensaje");

    const modalMensaje =
        document.getElementById("modalMensaje");

    const formulario =
        document.getElementById("formularioContacto");


    // =====================================================
    // ABRIR MODAL
    // =====================================================

    if (btnAbrirMensaje && modalMensaje) {

        btnAbrirMensaje.addEventListener("click", () => {

            modalMensaje.classList.add("active");

            document.body.style.overflow = "hidden";

        });

    }


    // =====================================================
    // CERRAR MODAL
    // =====================================================

    function cerrarModal() {

        if (modalMensaje) {

            modalMensaje.classList.remove("active");

        }

        document.body.style.overflow = "";

    }


    // =====================================================
    // BOTÓN X
    // =====================================================

    if (btnCerrarMensaje) {

        btnCerrarMensaje.addEventListener("click", () => {

            cerrarModal();

        });

    }


    // =====================================================
    // BOTÓN CANCELAR
    // =====================================================

    if (btnCancelarMensaje) {

        btnCancelarMensaje.addEventListener("click", () => {

            cerrarModal();

        });

    }


    // =====================================================
    // CERRAR AL HACER CLIC FUERA
    // =====================================================

    if (modalMensaje) {

        modalMensaje.addEventListener("click", (event) => {

            if (event.target === modalMensaje) {

                cerrarModal();

            }

        });

    }


    // =====================================================
    // CERRAR CON ESC
    // =====================================================

    document.addEventListener("keydown", (event) => {

        if (event.key === "Escape") {

            cerrarModal();

        }

    });


    // =====================================================
    // ENVIAR FORMULARIO SIN RECARGAR
    // =====================================================

    if (formulario) {

        formulario.addEventListener("submit", async (event) => {

            event.preventDefault();


            const boton =
                formulario.querySelector(".btn-send");


            // Desactivar botón

            boton.disabled = true;

            boton.textContent = "Enviando...";


            // Obtener datos

            const datos =
                new FormData(formulario);


            try {

                const respuesta = await fetch(
                    "/mensaje/enviar",
                    {
                        method: "POST",
                        body: datos
                    }
                );


                // Comprobar respuesta

                if (!respuesta.ok) {

                    throw new Error(
                        "Error al enviar el mensaje"
                    );

                }


                const resultado =
                    await respuesta.json();


                // =================================================
                // MENSAJE ENVIADO CORRECTAMENTE
                // =================================================

                if (resultado.exito) {


                    // Limpiar formulario

                    formulario.reset();


                    // Cerrar modal

                    cerrarModal();


                    // Mostrar notificación

                    mostrarNotificacion(
                        "✓ ¡Mensaje enviado correctamente!"
                    );

                }


            } catch (error) {

                console.error(
                    "Error al enviar:",
                    error
                );


                mostrarNotificacion(
                    "✕ No se pudo enviar el mensaje.",
                    true
                );


            } finally {

                // Reactivar botón

                boton.disabled = false;

                boton.textContent =
                    "📩 Enviar mensaje";

            }

        });

    }

});


// =====================================================
// NOTIFICACIÓN
// =====================================================

function mostrarNotificacion(
    texto,
    error = false
) {

    // Crear notificación

    const notificacion =
        document.createElement("div");


    notificacion.className =
        "notificacion";


    // Si es error

    if (error) {

        notificacion.classList.add("error");

    }


    // Contenido

    notificacion.innerHTML = `

<span>${texto}</span>

<button
    type="button"
    aria-label="Cerrar notificación">
    ×
</button>

    `;


    // Agregar al documento

    document.body.appendChild(
        notificacion
    );


    // Botón cerrar

    const botonCerrar =
        notificacion.querySelector("button");


    botonCerrar.addEventListener(
        "click",
        () => {

            cerrarNotificacion(
                notificacion
            );

        }
    );


    // Animación de entrada

    setTimeout(() => {

        notificacion.classList.add(
            "mostrar"
        );

    }, 10);


    // Desaparecer después de 4 segundos

    setTimeout(() => {

        cerrarNotificacion(
            notificacion
        );

    }, 4000);

}


// =====================================================
// CERRAR NOTIFICACIÓN
// =====================================================

function cerrarNotificacion(
    notificacion
) {

    if (!notificacion) return;


    notificacion.classList.remove(
        "mostrar"
    );


    setTimeout(() => {

        if (notificacion.parentElement) {

            notificacion.remove();

        }

    }, 400);

}

