package ar.edu.unahur.obj2.vendedores

class Certificacion(val esDeProducto: Boolean, val puntaje: Int)

abstract class Vendedor {
  // Acá es obligatorio poner el tipo de la lista, porque como está vacía no lo puede inferir.
  // Además, a una MutableList se le pueden agregar elementos
  val certificaciones = mutableListOf<Certificacion>() // lista - colección del tipo Certificacion

  // Definimos el método abstracto.
  // Como no vamos a implementarlo acá, es necesario explicitar qué devuelve.
  // En este caso devuelve un booleano (true/false)
  abstract fun puedeTrabajarEn(ciudad: Ciudad): Boolean

  // En las funciones declaradas con = no es necesario explicitar el tipo
  // En este caso devuelve un booleano (true/false)
  fun esVersatil() =
    certificaciones.size >= 3
      && this.certificacionesDeProducto() >= 1
      && this.otrasCertificaciones() >= 1

  // Si el tipo no está declarado y la función no devuelve nada, se asume Unit (es decir, vacío)
  fun agregarCertificacion(certificacion: Certificacion) {
    certificaciones.add(certificacion)
  }

  // Esta función devuelve un booleano (true/false)
  fun esFirme() = this.puntajeCertificaciones() >= 30

  // Esta función devuelve un número entero: Int
  fun certificacionesDeProducto() = certificaciones.count { it.esDeProducto }
  // Esta función devuelve un número entero: Int
  fun otrasCertificaciones() = certificaciones.count { !it.esDeProducto }
  // Esta función devuelve un número entero, ya que 'puntaje' es tipo Int:
  fun puntajeCertificaciones() = certificaciones.sumBy { c -> c.puntaje }
}

// En los parámetros, es obligatorio poner el tipo
class VendedorFijo(val ciudadOrigen: Ciudad) : Vendedor() {
  override fun puedeTrabajarEn(ciudad: Ciudad): Boolean {
    return ciudad == ciudadOrigen
  }
  // Esta función devuelve un booleano (false)
  fun esInfluyente() = false
}

// A este tipo de List no se le pueden agregar elementos una vez definida
class Viajante(val provinciasHabilitadas: List<Provincia>) : Vendedor() {
  override fun puedeTrabajarEn(ciudad: Ciudad): Boolean {
    return provinciasHabilitadas.contains(ciudad.provincia)
  }
  // Esta función devuelve un booleano (true/false)
  fun esInfluyente() = provinciasHabilitadas.sumBy { it.poblacion } >= 10000000
}

class ComercioCorresponsal(val ciudades: List<Ciudad>) : Vendedor() {
  override fun puedeTrabajarEn(ciudad: Ciudad): Boolean {
    return ciudades.contains(ciudad)
  }
  val provinciasHabilitadas = ciudades.map { it.provincia }.toSet()

  // Esta función devuelve un booleano (true/false)
  fun esInfluyente() =
    ciudades.size >= 5
      || provinciasHabilitadas.size >= 3
}


class CentroDistribucion(val ciudad: Ciudad) {
  // Es una lista mutable de sub-instancias de Vendedor
  val vendedores = mutableListOf<Vendedor>()

  // La función devuelve un booleano (true/false)
  fun vendedorRegistrado(vendedor: Vendedor): Boolean {
    return vendedores.contains(vendedor)
  }

  // Esta función no devuelve nada, pero genera un mensaje de error
  // cuando el vendedor ya está registrado en el centro.
  fun agregarVendedor(vendedor: Vendedor) {
    check(!vendedorRegistrado (vendedor)) {
      "El vendedor ya está registrado para el centro"
       }
    vendedores.add(vendedor)
  }
  // Esta función devuelve una instancia de Vendedor
  fun vendedorEstrella() = vendedores.maxBy { it.puntajeCertificaciones() }

  // Esta función devuelve un booleano (true/false)
  fun puedeCubrir(ciudad: Ciudad) = vendedores.any { it.puedeTrabajarEn(ciudad) }

  // Esta función devuelve una colección de vendedores
  fun vendedoresGenericos() = vendedores.filter { it.otrasCertificaciones() > 0 }

  // Esta función devuelve un booleano (true/false)
  fun esRobusto() = vendedores.count { it.esFirme() } >= 3
}