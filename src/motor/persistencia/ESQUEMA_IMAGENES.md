E1 = id_imagen(pk) + path + { nombre_recorte(pk) + offset_x + offset_y + ancho + alto }
+ { nombre_animacion(pk) + esBucle + { nro_frame(pk) + nombre_recorte + duracion }}

1FN

E1.1 = id_imagen(pk) + path 
E1.2 = id_imagen(pk) + nombre_recorte(pk) + offset_x + offset_y + ancho + alto
E1.3 = id_imagen(pk) + nombre_animacion(pk) + esBucle
E1.4 = id_imagen(pk) + nombre_animacion(pk) + nro_frame(pk) + nombre_recorte + duracion

2FN

E1.1.0 = id_imagen(pk) + path 
E1.2.0 = id_imagen(pk) + nombre_recorte(pk) + offset_x + offset_y + ancho + alto
E1.3.0 = id_imagen(pk) + nombre_animacion(pk) + esBucle
E1.4.0 = id_imagen(pk) + nombre_animacion(pk) + nro_frame(pk) + nombre_recorte + duracion

3FN

E1.1.0.0 = id_imagen(pk) + path 
E1.2.0.0 = id_imagen(pk) + nombre_recorte(pk) + offset_x + offset_y + ancho + alto
E1.3.0.0 = id_imagen(pk) + nombre_animacion(pk) + esBucle
E1.4.0.1 = id_imagen(pk) + nombre_animacion(pk) + nro_frame(pk) + nombre_recorte(FK) + duracion

RENOMBRAMIENTO:

imagen = E1.1.0.0 = id_imagen(pk) + path 
recorte = 1.2.0.0 = id_imagen(pk) + nombre_recorte(pk) + offset_x + offset_y + ancho + alto
animacion = E1.3.0.0 = id_imagen(pk) + nombre_animacion(pk) + esBucle
frame = E1.4.0.1 = id_imagen(pk) + nombre_animacion(pk) + nro_frame(pk) + nombre_recorte(FK) + duracion