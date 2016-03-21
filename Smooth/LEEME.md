# Algoritmo de suavizado de curvas
Esta es la entrada en [Stackoverflow](http://stackoverflow.com/questions/5525665/smoothing-a-hand-drawn-curve).

1. Primero se aplica un algoritmo para reducir el número de puntos sobre el trazo [Wikipedia: Ramer–Douglas–Peucker](https://en.wikipedia.org/wiki/Ramer%E2%80%93Douglas%E2%80%93Peucker_algorithm).
2. Después se ajusta a una serie de curvas de Bezier utilizando el algoritmo de Philip J Schneider que se puede encontrar en el último capítulo de Graphics Gems Vol I, último capítulo.

La entrada en la Wikipedia tiene un vídeo clarificador.

# Algoritmo de Ramer-Douglass-Peucker
Es un algoritmo que se basa en eliminar los puntos que estén más alejados que un cierto umbral, de una curva de cierto «grosor». La entrada de la Wikipedia es muy clarificadora.

# Algoritmo de ajuste a curvas de Bezier (cúbicas)
El siguiente paso, una vez que se ha reducido en número de puntos, es ajustar los puntos restantes a una curva o conjunto de curvas de Bezier cúbicas.

He utilizado el capítulo 18 de Graphics Gems, primer tomo: An algorithm ofr automatically fitting digitized curves.

## Parametrización por longitud de cuerda
El primer paso es parametrizar nuestro conjunto de puntos. Para ello se puede utilizar la [parametrización por longitud de cuerda](http://www.cs.mtu.edu/~shene/COURSES/cs3621/NOTES/INT-APP/PARA-chord-length.html).

La página marvillosa cuenta todo sobre [curvas de Bezier](http://pomax.github.io/bezierinfo/), y tiene unos estupendos applets muy descriptivos, que he utilizado para, por ejemplo, validar que la longitud de las curvas de Bezier que yo calculaba era correcta. Además, tiene una referencia a un artículo que muestra [cómo moverse a lo largo de una curva a una velocidad fija](https://www.geometrictools.com/Documentation/MovingAlongCurveSpecifiedSpeed.pdf), donde se muestra la idea de reparametrización de una curva y cómo calcularla.

Para calcular la longitu de una curva de Bezier necesitamos calcular una integran definida. Y para esto he utilizado [Apache Commons Math](https://commons.apache.org/proper/commons-math/)

Apache Commons Math es tan alucinante que tiene un paquete para calcular derivadas utilizando [Derivative Structures](http://www1.american.edu/cas/mathstat/People/kalman/pdffiles/mmgautodiff.pdf) que es un artículo donde se presenta este concepto matemático, que es muy, muy interesante.