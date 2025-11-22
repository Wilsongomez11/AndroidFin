/**import com.example.proyectofinal.Model.Caja
import com.example.proyectofinal.Model.MovimientoCaja
import io.ktor.http.cio.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CajaService {

    @GET("movimientos/hoy")
    suspend fun getMovimientosHoy(): List<MovimientoCaja>

    @GET("caja/hoy")
    suspend fun getCajaDelDia(): Caja

    @POST("pagos/registrar")
    suspend fun registrarPago(
        @Query("pedidoId") pedidoId: Long,
        @Query("montoPagado") montoPagado: Double,
        @Query("adminId") adminId: Long
    ): Response<Map<String, Any>>
}**/
