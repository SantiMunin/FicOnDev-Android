Ordroid
================

Omg wtf this name <- yep, we know.

Descripcion
--------------
Se trata de una app que ofrece el dueño de la empresa a distintos distribuidores (a los que les da un usuario en el sistema). Una vez que se logean pueden pedir pedidos y seguir el estado de los mismos (pendientes de preparado, preparados para ser recogidos o ya recogidos por sus empleados).

Además permite hacer pedidos de productos.


Características
---------------
	- Implementación hiper molona de la capa de comunicaciones utilizando callbacks (ver es.udc.smunin.empresauriostic.ordermanager.model.OperationsManager).
	- Uso de una API REST con logeo ([ficondev]["LINK AL API REST"]).
	- Abstracción de la manipulación del Preferences en es.udc.smunin.empresauriostic.ordermanager.service.
	- Uso de notificaciones (singulares y stackeadas) <- generadas por un polling cada 30 segundos (para probar fácil) al servidor
	- Uso de la ActionbarSherlock (compatible con android 2.2 <- comprobado).
	- Uso de dialogos de carga para dar al usuario impresión de dinamismo.
	- Login automático (una vez logeado no tiene que volver a hacerlo.
	- Images adaptadas a varias pantllas (4 tamaños distintos).

[ficondev][https://github.com/iaguis/API-ficondev-bebidas]
