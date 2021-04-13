package ar.edu.unahur.obj2.vendedores

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldHave
import io.kotest.matchers.shouldNotHave

class VendedorTest : DescribeSpec({

  // declaro dos certificaciones para asignar a varios vendedores
  val certificacion_5p = Certificacion(false, 5)
  val certificacion_10p = Certificacion(true,10)
  val certificacion_20p = Certificacion(true, 20)

  val misiones = Provincia(1300000)
  val sanIgnacio = Ciudad(misiones)

  // agrego varias provincias y ciudades para reutilizarlas en varios test
  // Salta
  val salta = Provincia(535303) // según Censo 2010
  val cafayate = Ciudad(salta)
  val cachi = Ciudad(salta)
  // Chubut
  val chubut = Provincia(506668)  // según Censo 2010
  val comodoroRivadavia = Ciudad(chubut)
  val esquel = Ciudad(chubut)

  // declaro por anticipado los valores de "Vendedor fijo" para reutilizarlos en otros test
  val obera = Ciudad(misiones)
  val vendedorFijo = VendedorFijo(obera)
  // le agrego una certificación
  vendedorFijo.agregarCertificacion(certificacion_5p)

  describe("Vendedor fijo") {
    // val obera = Ciudad(misiones)
    // val vendedorFijo = VendedorFijo(obera)

    describe("puedeTrabajarEn") {
      it("su ciudad de origen") {
        vendedorFijo.puedeTrabajarEn(obera).shouldBeTrue()
      }
      it("otra ciudad") {
        vendedorFijo.puedeTrabajarEn(sanIgnacio).shouldBeFalse()
      }
    }
  }

  describe("Viajante") {
    val cordoba = Provincia(2000000)
    val villaDolores = Ciudad(cordoba)
    val viajante = Viajante(listOf(misiones))

    describe("puedeTrabajarEn") {
      it("una ciudad que pertenece a una provincia habilitada") {
        viajante.puedeTrabajarEn(sanIgnacio).shouldBeTrue()
      }
      it("una ciudad que no pertenece a una provincia habilitada") {
        viajante.puedeTrabajarEn(villaDolores).shouldBeFalse()
      }
    }
    // agrego test para la función "esInfluyente"
    describe("esInfluyente") {
      it("no supera los 10 millones") {
        viajante.esInfluyente().shouldBeFalse()
      }
    }
  }

  // agrego varios tests para un comercio corresponsal
  describe("Corresponsal") {
    val comercio = ComercioCorresponsal(listOf(cafayate, esquel, comodoroRivadavia, sanIgnacio))

    describe("puedeTrabajarEn") {
      it("una ciudad donde tiene sucursal") {
        comercio.puedeTrabajarEn(cafayate).shouldBeTrue()
      }
      it("una ciudad donde no tiene sucursal") {
        comercio.puedeTrabajarEn(cachi).shouldBeFalse()
      }
    }
    describe("esInfluyente") {
      it("tiene al menos sucursales en tres provincias") {
        comercio.esInfluyente().shouldBeTrue()
      }
    }
  }

  // agrego varios test para un Centro de Distribución
  describe("CentroDistribucion") {
    val centroCafayate = CentroDistribucion(cafayate)
    val vendedor1 = VendedorFijo(comodoroRivadavia)
    val vendedor2 = Viajante(listOf(chubut, misiones, salta))
    centroCafayate.agregarVendedor(vendedorFijo)
    centroCafayate.agregarVendedor(vendedor1)

    describe("agregarVendedor") {
      it("no registrado") {
        centroCafayate.agregarVendedor(vendedor2).shouldNotBeNull()
      }
      it("no permite agregar vendedor ya registrado") {
        shouldThrowAny { centroCafayate.agregarVendedor(vendedor2) }
      }
    }
    describe("esRobusto") {
      it("No tiene al menos tres vendedores firmes") {
        centroCafayate.esRobusto().shouldBeFalse()
      }
    }
    describe("vendedorGenerico") {
      it("Hay solo un vendedor genérico: vendedorFijo") {
        centroCafayate.vendedoresGenericos().shouldBe(listOf(vendedorFijo))
      }
    }
    describe("vendedorEstrella") {
      vendedor2.agregarCertificacion(certificacion_10p)
      it("El vendedor2 es estrella") {
        centroCafayate.vendedorEstrella().shouldBe(vendedor2)
      }
    }
    describe("puedeCubrir") {
      it("El Centro de Distribución puede cubrir la ciudad de Esquel") {
        centroCafayate.puedeCubrir(esquel).shouldBeTrue()
      }
    }
    describe("Nuevo vendedorEstrella") {
      vendedor1.agregarCertificacion(certificacion_20p)
      it("Ahora el vendedor1 es estrella") {
        centroCafayate.vendedorEstrella().shouldBe(vendedor1)
      }
    }
  }
})
