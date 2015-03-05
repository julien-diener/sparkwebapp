A very simple spring web app that use apache spark

The goal is to take the [spark/hdfs csvconverter](https://github.com/julien-diener/spark-csvconverter)
and make it run from a simple spring servlet.

The main trouble come from conflict between the spark and hadoop dependencies, and maybe the spring ones.
In the previous stand alone app [csvconverter](https://github.com/julien-diener/spark-csvconverter), the dependencies
were only used for compilation, but at run time another, global, jar is used: the
`$SPARK_HOME/lib/spark-assembly-1.1.1-hadoop2.4.0.jar` provided by the spark installation.

The solution used here is to replace the maven dependencies included in the war by this
spark-assembly jar. See this [SO answer](http://stackoverflow.com/a/28866218/1206998) for more details.

Author [Julien Diener](http://julien.diener.website)
