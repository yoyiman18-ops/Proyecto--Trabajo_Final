# Motor

El motor del juego permite administrar los frames (tanto lógicos como de renderizado) en el bucle del juego, la lógica de colisiones, las inputs del teclado, la carga de recursos, y la creación de objetos relevantes al juego.

## Colisiones

Aquellos objetos que tengan lógica de colisión deben implementar la interfaz Colisionable. 

Por ahora, se implementan las siguientes características:

- Hitboxes: Sí
- Capas de colisión: Sí
- Intersecciones entre hitboxes: Sí
- Optimización espacial: Sí (SpatialHashGrid)
- Fase general (evaluación de posibles pares de objetos que podrían colisionar): Sí
- Fase específica (evaluar de entre los pares generados cuáles realmente colisionan): Sí
- Nivel de penetración: Por implementar
- Sweep and prune (calcular todas las celdas donde podría estar un objeto dependiendo de su Hitbox, posición y velocidad, básicamente para evitar tunneling): Por implementar
- Momento de intersección: Por implementar


### Hitbox

Las colisiones requieren de una Hitbox, implementada con la lógica geométrica de java.awt. 

La clase abstracta Hitbox incluye un método de intersección genericoConGenerico(Hitbox otra) que retorna un booleano según el área transformada de su propia figura intersecte el área de la figura transformada de la otra Hitbox; este método funciona para cualquier figura pero es relativamente costoso, por ello es conveniente que clases concretas que hereden de Hitbox implementen lógica optimizada para figuras comúnmente utilizadas. Por este motivo es que se proveen las implementaciones HitboxGenerica y HitboxRectangular, ambas optimizadas para operar con figuras rectangulares o figuras genéricas.

A su vez, la Hitbox determina sus propias categorías y las categorías con las que puede colisionar con dos MascaraColision, implementadas con máscaras de bits de 32 bits. Una MascaraColision debe instanciarse con el método MascaraColision.of(CategoriaColision... categorias), que automáticamente forma la bitmask según las posiciones ordinales de los valores del enum CategoriaColision. 

La Hitbox también dispone de un TipoHitbox, relevante tanto para determinar qué debería hacer un objeto al colisionar con otro según su tipo de hitbox, así como para evitar cálculos de Hitbox inamovible con otra Hitbox inamovible.

Al cambiar de posición un objeto, debe de actualizar la transformación de su Hitbox con actualizarTransformacion(VecDouble2D posicion). De otro modo, su Hitbox quedará en una posición que no corresponde con la posición del objeto.