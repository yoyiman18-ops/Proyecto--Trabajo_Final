# Proyecto: Waves 2D

## 1. Integrantes del Equipo

- Durante Baldessari Valentín
- Gomez Lautaro
- Ibarrola Enzo
- Rodríguez Joel

## 2. Dominio y Alcance del Sistema

### Descripción del Problema

Se busca desarrollar un videojuego de escritorio basado en el género "Roguelike". El jugador debe sobrevivir a un número limitado de oleadas aleatorias de enemigos con dificultad incremental en un mapa cerrado, buscando destruirlos ellos y recolectar mejoras aleatorias hasta acabar con todas las oleadas y finalizar la partida.

### Objetivo del Sistema

El sistema será un juego funcional que implemente las características del género "Roguelike", con un diseño orientado a la escalabilidad que permita la adición de nuevos tipos de personajes, enemigos y mejoras; respetando los cuatro pilares de la programación orientada a objetos (abstracción, encapsulación, herencia y polimorfismo).

### Funcionalidades Principales (Features)

- **Diseño del motor del videojuego**
    - El motor implementa "frames" (cuadros) de físicas para calcular la lógica correspondiente a las físicas del juego de forma consistente.
    - Los gráficos 2D del juego son procesados y dibujados en los frames de renderizado correspondientes, a través de la liberaria Java Swing.
    - Se implementa la obtención, lectura y almacenamiento del estado de teclas relevantes para un instante dado.
- **Mecánicas de juego**
    - La jugabilidad se centra en la supervivencia un número finito oleadas de enemigos y la obtención de mejoras a través de la experiencia (recurso obtenido de los enemigos destruidos).
    - El personaje jugable tiene una cantidad de puntos de vida limitados, que al reducirse a cero finalizan la partida.
    - Se presentan múltiples personajes básicos, cada uno con atributos iniciales distintos.
- **Sistema de mejoras:**
    - Las mejoras modifican los atributos y las habilidades del personaje del jugador.
    - La experiencia obtenida permite al jugador subir automáticamente de nivel, presentándole tres opciones de mejoras aleatorias desde las opciones disponibles.
- **Sistema de enemigos:**
    - Los enemigos tienen una inteligencia artificial básica para atacar al jugador, perisiguiéndolo o disparando proyectiles.
    - Existen distintos tipos de enemigos, con atributos y habilidades distintas. 
    - Se obtiene experiencia al destruir enemigos.
    - Se generan oleadas de enemigos en el mapa cerrado en base a un cronómetro. Los enemigos aparecen a una distancia mínima del jugador. Las oleadas eligen aleatoriamente entre grupos surtidos de enemigos predefinidos para generar cada oleada. Acabar con todos los enemigos antes que inicie la próxima oleada otorga experiencia adicional.
    - La dificultad de los enemigos incrementa a medida que se avanza en la partida.
- **Intefaz Gráfica (GUI)**
    - Interfaz de atributos y mejoras obtenidas del jugador durante la partida.
    - Contador de enemigos restantes y cronómetro de tiempo restante antes que inicie la próxima oleada.
    - Panel de selección de mejoras al subir de nivel.
    - Selector de personaje antes de iniciar una partida.
- **Persistencia:**
    - Sistema de guardado y carga que registra la experiencia total obtenida del jugador.

## 3. Arquitectura de Diseño
Por definir.

## 4. Stack Tecnológico
- **Lenguaje:** Java 23
- **IDE:** Visual Studio Code
- **Base de Datos:** MySQL 8.0
- **Framework de IGU:** Java Swing
- **Control de Versiones:** Git y GitHub


