package cr.ac.menufragmentcurso.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import cr.ac.menufragmentcurso.R
import cr.ac.menufragmentcurso.entity.Empleado

class EmpleadoAdapter(context:Context, empleados:List<Empleado>):
    ArrayAdapter<Empleado>(context,0,empleados) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater : LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val rowView = inflater.inflate(R.layout.empleado_item, parent, false)

        val imagen = rowView.findViewById<ImageView>(R.id.empleado_imagen)
        val nombre = rowView.findViewById<TextView>(R.id.empleado_nombre)
        val puesto = rowView.findViewById<TextView>(R.id.empleado_puesto)

        val empleado = getItem(position)

        nombre.setText(empleado?.nombre)
        puesto.setText(empleado?.puesto)
        if(empleado?.avatar != ""){
            imagen.setImageBitmap(empleado?.avatar?.let { decodeImage(it) })
        }


        return rowView
    }


    private fun decodeImage (b64 : String): Bitmap {
        val imageBytes = Base64.decode(b64, 0)
        return  BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}