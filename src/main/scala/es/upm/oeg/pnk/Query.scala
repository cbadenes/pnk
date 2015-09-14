package es.upm.oeg.pnk

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.feature.Word2VecModel
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by cbadenes on 05/08/15.
 */
object Query {

  def main(args: Array[String]): Unit = {

    // Spark Configuration
    val conf = new SparkConf().
      setMaster("local[4]").
      setAppName("Local Spark Example").
      set("spark.executor.memory", "48g").
      set("spark.driver.maxResultSize","8g")
    val sc = new SparkContext(conf)

    Logger.getRootLogger.setLevel(Level.WARN)

    println(s"loading word2vec model to detect similar words..")
    val w2vModel    = Word2VecModel.load(sc, Analyze.w2v_optimized)

    val places = List(
      "Andalucia",
      "Madrid",
      "Ceuta",
      "Melilla",
      "Valdemoro",
      "Tres_Cantos",
      "Rozas",
      "Rivas",
      "Majadahonda", //Majadahonda_Madrid
      "Pals",
      "España",
      "Singapur",
      "Suiza"
    )


    val organizations = List(
      "Confederación_Suiza",
      "Aeat",
      "lglesia_Católica",
      "Arauna_Corporacion_Y_Consulting_Sl",
      "Alcolea_Capital_Gestiones",
      "Gold_And_Diamond_Joyeros_Sl",
      "Divana_Integral_Sl",
      "Sheraton_Trading_S.A",
      "Atención_Social_Y_Residencia_Valdemoro_Sl",
      "Aserval_Inversiones_E_Iniciativas_Inmobiliarias",
      "Anahid_Gestion_Sl",
      "Dara_Integral_Sl",
      "Gimor_Gestion_Sl",
      "Obrum_Urbanismo_Y_Construcciones_Sl",
      "Deloya_Gestion_Sl",
      "Servicios_Logisticos_Promocion_Innovaciones_Aranda_Corporacion",
      "Vancouver_Gestion",
      "Guardia_Civil",
      "Devadai_Sl",
      "Sheraton_Trading_S.A",
      "Sindicato_Manos_Limpias"
    )



    val people = List(
      "Agustín_Juarez_López_de_Coca",
      "Alberto_Sánchez_Caballero",
      "Alejandro_de_Pedro_Llorca",
      "Alejandro_Utrilla_Palombi",
      "Alfredo_Ovejero_López",
      "Ana_María_Ramírez_Fernández",
      "Ana_Mayoral_Blaya",
      "Antonio_Borrego_Fortea",
      "Antonio_Brosed_Abardía",
      "Antonio_Cándido_Ruiz_García",
      "Antonio_Sánchez_Fernández",
      "Antonio_Serrano_Soldado",
      "Avelino_Pérez_Pallarés",
      "Constantino_Álvarez_de_la_Cueva",
      "David_Marjaliza_Villaseñor",
      "David_Rodríguez_Sanz",
      "Didier_Maurice",
      "Eduardo_de_la_Peña_Pérez",
      "Elena_María_Fernández_Pérez",
      "Francisco_Eduardo_Ruiz_Valenzuela",
      "Francisco_Javier_Bueno_González",
      "Francisco_José_Granados_Lerena",
      "Gonzalo_Cubas_Navarro",
      "Jesús_Norberto_Galindo_Sánchez",
      "José_Antonio_Alonso_Conesa",
      "José_Antonio_Hernández_Pérez",
      "José_Carlos_Boza_Lechuga",
      "José_Fidel_Saura_Guerrero",
      "José_Javier_Hernández_Nieto",
      "José_Luis_Huerta_Valbuena",
      "José_Luis_Navarro_Soto",
      "José_Manuel_Casado_Garzón",
      "José_Manuel_Rodríguez_Talamino",
      "José_María_Fraile_Campos",
      "José_Martínez_Nicolás",
      "Juan_José_Sanchez_Barceló",
      "Manuel_Borreguero_Roldán",
      "Manuel_Jesús_López_Sánchez",
      "Manuel_Selles",
      "María_Nieves_Alarcón_Castellanos",
      "Maria_Reyes_Samper_Henajeros",
      "Mariola_Martínez_Robles",
      "Martín_Marcos_Martínez_Barazón",
      "Pedro_García_Pérez",
      "Rafael_Laso_Retamar",
      "Ramiro_Cid_Sicluna",
      "Santiago",
      "Sara_Sánchez_Hervás",
      "Víctor_Manuel_Ortega_Martínez",
      "Victoria_Muñoz_Aguera"
    )

    val terms = List(
      "delito",
      "investigada",
      "dinero",
      "soborno",
      "paraíso",
      "Corrupción",
      "Querella",
      "criminal",
      "fraude",
      "malversación",
      "prevaricación",
      "entramado",
      "implicadas",
      "Enjuiciamiento",
      "Blanqueo",
      "enriquecimiento",
      "Antecedentes_Policiales",
      "Ipad", //Tablet_Ipad
      "Iphone",
      "Real_Madrid"
    )



    List(("Personas",people),("Lugares",places),("Empresas",organizations),("Palabras_Clave",terms)).foreach{case (description,words)=>
      println(s"=================== $description:")
      words.foreach{case word=>
        try {
          println("'" + word + "': ")
          w2vModel.findSynonyms(word, 40).filter(x=>x._1.length < 80).toList.foreach(x=>println("\t- " +x._1 + "\t[" +x._2 +"]"))
        }catch {
          case e:Exception => println(s"\t not in vocabulary")
        }
      }
    }




  }

}
