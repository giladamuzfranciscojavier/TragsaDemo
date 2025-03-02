# TragsaDemo

Este proyecto se trata de una pequeña demo técnica con el objetivo de demostrar conocimientos de Java, SQL y tests unitarios con JUnit, además del [uso de la herramienta de integración contínua GitHub Actions](https://github.com/giladamuzfranciscojavier/TragsaDemo/actions).


## Escenario demo

Se trata de un escenario imaginario sin base real, por lo que no tiene demasiado sentido ni utilidad práctica, pero sirve para cumplir con el propósito del proyecto. Se puede resumir en los siguientes puntos:

- El escenario tratado consiste en dos categorías de usuarios: Clientes y Proveedores; y Productos. 
- Un Cliente puede comprar varios productos o ninguno
- Un proveedor puede proporcionar varios productos o ninguno
- Un producto puede ser comprado o no por un cliente, y siempre debe tener un proveedor. 
- Un usuario puede ser cliente y proveedor al mismo tiempo (se permite borrar ambas "identidades" al mismo tiempo).
- Se puede "devolver" un producto (se elimina la relación con el cliente)
- Si se borra un proveedor se borrarán también los productos que este suministre


## Problemas durante el desarrollo

Debido a la naturaleza del proyecto y la corta duración de su desarrollo (alrededor de 2 días) existen algunos problemas de diseño, tanto accidentales como intencionales, que han requerido soluciones poco elegantes.

Un ejemplo de la primera categoría es la dificultad de localizar un producto sin realizar previamente una consulta de toda la tabla, ya que su clave primaria se genera de forma automática. Esto supone una dificultad elevada a la hora de realizar las pruebas, ya que es imposible predecir los índices. La solución encontrada a este problema ha sido truncar la tabla Producto antes de cada prueba realizada sobre la misma.

Un ejemplo de la segunda categoría es el uso de sentencias SQL en el código, en lugar de emplear una herramienta de mapeo objeto-relacional como Hibernate (utilizado en [este proyecto](https://github.com/giladamuzfranciscojavier/LemTamApp)). Esto implica una mayor complejidad del código y, por lo tanto, mayor dificultad de desarrollo y un hipotético mantenimiento futuro. El propósito de este enfoque es demostrar un conocimiento adecuado de conceptos básicos de SQL, como la realización de operaciones CRUD, el uso de claves foráneas y consultas sobre varias tablas.
