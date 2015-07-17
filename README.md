# ZombieLand
Videojuego realizado como trabajo práctico final para el taller de programación avanzada. Julio 2015

##Información sobre la construcción del juego:
- Base de datos MySQL, la cual aloja los datos de auntenticación de cada jugador y sus valores estadíasticos (cantidad de partidas ganadas, puntaje, etc.)
- RegEx, utilizadas en la ventana LogIn del cliente para la validación de la dirección IP del servidor.
- Threads, utilizados para la sincronización de mensajes y las cuentas regresivas que determinan el inicio de cada nuevo turno de juego.
- Clase gestora que mensajes, la cual se relaciona tanto con los thread de comunicación como con todas las ventanas con las que interactúa el usuario.
- Lógica del juego centralizada en el servidor, el cual verifica colisiones y conversiones de humano a zombie. Además, efectúa la detección y tratamiento de clientes desconectados, y la asignación de puntos al finalizar la partida.
- Patrón de diseño composite, utilizado para la interacción con las distintas piezas del tablero.

###Autores:
- Massiolo, Rodrigo Héctor
- Mignone, Mattias Ezequiel
- Vasallo, Diego
- Vázquez, Fernando Nicolás
