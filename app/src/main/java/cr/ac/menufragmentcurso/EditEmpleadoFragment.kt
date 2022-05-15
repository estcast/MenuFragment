package cr.ac.menufragmentcurso

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.drawable.toBitmap
import com.squareup.picasso.Picasso
import cr.ac.menufragmentcurso.entity.Empleado
import cr.ac.menufragmentcurso.repository.EmpleadoRepository
import java.io.ByteArrayOutputStream
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val PICK_IMAGE = 100

/**
 * A simple [Fragment] subclass.
 * Use the [EditEmpleadoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditEmpleadoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var empleado: Empleado? = null
    lateinit var img_avatar: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            empleado = it.get(ARG_PARAM1) as Empleado?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_edit_empleado, container, false)

        var nombre : EditText = view.findViewById(R.id.nombre_textplain)
        nombre.setText(empleado?.nombre)

        var id : EditText = view.findViewById(R.id.id_textplain)
        id.setText(empleado?.identificacion)

        var puesto : EditText = view.findViewById(R.id.puesto_textplain)
        puesto.setText(empleado?.puesto)

        var dept : EditText = view.findViewById(R.id.dept_textplain)
        dept.setText(empleado?.departamento)

        img_avatar = view.findViewById(R.id.avatar)

        if(empleado?.avatar != ""){
            img_avatar.setImageBitmap(empleado?.avatar?.let { decodeImage(it) })
        }


        var button_modificar : Button = view.findViewById(R.id.button_modificar)

        button_modificar.setOnClickListener{

            val builder = AlertDialog.Builder(context)
            val fragmento : Fragment = CameraFragment.newInstance(getString(R.string.menu_camera))

            builder.setMessage("¿Desea modificar el registro?")
                .setCancelable(false)
                .setPositiveButton("Sí") { dialog, id ->

                    val i:EditText = view.findViewById(R.id.id_textplain)
                    val nomb:EditText = view.findViewById(R.id.nombre_textplain)
                    val puest:EditText = view.findViewById(R.id.puesto_textplain)
                    val departa:EditText = view.findViewById(R.id.dept_textplain)

                    val e:Empleado = Empleado(empleado!!.idEmpleado, i.text.toString(),
                        nomb.text.toString(),puest.text.toString(),
                        departa.text.toString(),empleado!!.avatar)

                        e?.avatar = encodeImage(img_avatar.drawable.toBitmap())!!
                        EmpleadoRepository.instance.edit(e)



                    // código para regresar a la lista

                    fragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.home_content, fragmento)
                        ?.commit()
                }
                .setNegativeButton(
                    "No"
                ) { dialog, id ->
                    // logica del no

                }
            val alert = builder.create()
            alert.show()
        }

        var button_eliminar: Button = view.findViewById(R.id.button_eliminar)

        button_eliminar.setOnClickListener{

            val builder = AlertDialog.Builder(context)
            val fragmento : Fragment = CameraFragment.newInstance(getString(R.string.menu_camera))

            builder.setMessage("¿Desea eliminar el registro?")
                .setCancelable(false)
                .setPositiveButton("Sí") { dialog, id ->

                  //  val id:EditText = view.findViewById(R.id.id_textplain)
                    EmpleadoRepository.instance.delete(empleado!!.idEmpleado)

                    // código para regresar a la lista

                    fragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.home_content, fragmento)
                        ?.commit()
                }
                .setNegativeButton(
                    "No"
                ) { dialog, id ->
                    // logica del no

                }
            val alert = builder.create()
            alert.show()

        }

        img_avatar.setOnClickListener{
            var gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery,PICK_IMAGE)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            var imageUri = data?.data
            Picasso.get()
                .load(imageUri)
                .resize(120,120)
                .centerCrop()
                .into(img_avatar)
        }
    }

    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT).replace("\n","")
    }

    private fun decodeImage (b64 : String): Bitmap{
        val imageBytes = Base64.decode(b64, 0)
        return  BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param empleado Parameter 1.
         * @return A new instance of fragment EditEmpleadoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(empleado:Empleado) =
            EditEmpleadoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, empleado)
                }
            }
    }
}