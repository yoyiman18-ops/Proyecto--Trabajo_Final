E1 = id_sheet(pk) + path + extension + { nombre_recorte(pk) + offset_x + offset_y + ancho + alto } + { nombre_animacion(pk) + esBucle + { nro_frame(pk) + nombre_recorte + duracion }}

1FN

E1.1 = id_sheet(pk) + path + extension
E1.2 = id_sheet(pk) + nombre_recorte(pk) + offset_x + offset_y + ancho + alto
E1.3 = id_sheet(pk) + nombre_animacion(pk) + esBucle
E1.4 = id_sheet(pk) + nombre_animacion(pk) + nro_frame(pk) + nombre_recorte + duracion

2FN

E1.1.0 = id_sheet(pk) + path + extension
E1.2.0 = id_sheet(pk) + nombre_recorte(pk) + offset_x + offset_y + ancho + alto
E1.3.0 = id_sheet(pk) + nombre_animacion(pk) + esBucle
E1.4.0 = id_sheet(pk) + nombre_animacion(pk) + nro_frame(pk) + nombre_recorte + duracion

3FN

E1.1.0.0 = id_sheet(pk) + path + extension
E1.2.0.0 = id_sheet(pk) + nombre_recorte(pk) + offset_x + offset_y + ancho + alto
E1.3.0.0 = id_sheet(pk) + nombre_animacion(pk) + esBucle
E1.4.0.1 = id_sheet(pk) + nombre_animacion(pk) + nro_frame(pk) + nombre_recorte(FK) + duracion

RENOMBRAMIENTO:

sheet = E1.1.0.0 = id_sheet(pk) + path + extension
recorte = 1.2.0.0 = id_sheet(pk) + nombre_recorte(pk) + offset_x + offset_y + ancho + alto
animacion = E1.3.0.0 = id_sheet(pk) + nombre_animacion(pk) + es_bucle
frame = E1.4.0.1 = id_sheet(pk) + nombre_animacion(pk) + nro_frame(pk) + nombre_recorte(FK) + duracion