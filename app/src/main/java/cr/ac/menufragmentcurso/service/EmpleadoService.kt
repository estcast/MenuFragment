package cr.ac.menufragmentcurso.service

import cr.ac.menufragmentcurso.entity.Empleado
import cr.ac.menufragmentcurso.entity.Record
import retrofit2.Call
import retrofit2.http.*

interface EmpleadoService {

    @GET("empleado")
    fun getEmpleado(): Call<Record>

    @PUT("empleado/{idEmpleado}")
    fun update(@Path("idEmpleado") idEmpleado:Int, @Body empleado:Empleado):Call<String>

    //@DELETE
    @DELETE("empleado/{idEmpleado}")
    fun delete(@Path("idEmpleado") idEmpleado:Int):Call<Int>


    //POST empleado create
    @POST("empleado")
    fun create(@Body empleado:Empleado):Call<Int>
}