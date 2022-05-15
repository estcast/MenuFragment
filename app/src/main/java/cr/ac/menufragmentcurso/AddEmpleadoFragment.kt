package cr.ac.menufragmentcurso

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import com.squareup.picasso.Picasso
import cr.ac.menufragmentcurso.entity.Empleado
import cr.ac.menufragmentcurso.repository.EmpleadoRepository
import java.io.ByteArrayOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER



private const val PICK_IMAGE = 100
/**
 * A simple [Fragment] subclass.
 * Use the [AddEmpleadoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddEmpleadoFragment : Fragment() {
    // TODO: Rename and change types of parameters
   // private var param1: String? = null
   // private var param2: String? = null

    lateinit var img_avatar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
          //  param1 = it.getString(ARG_PARAM1)
           // param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view : View = inflater.inflate(R.layout.fragment_add_empleado, container, false)


        img_avatar = view.findViewById(R.id.addimage_addempleado)

        var buttonAdd : Button = view.findViewById(R.id.addbutton_addempleado)
        buttonAdd.setOnClickListener{

            val builder = AlertDialog.Builder(context)
            val fragmento : Fragment = CameraFragment.newInstance(getString(R.string.menu_camera))

            builder.setMessage("¿Desea ingresar el registro?")
                .setCancelable(false)
                .setPositiveButton("Sí") { dialog, id ->

                    val id: EditText = view.findViewById(R.id.addid_editText)
                    val nomb: EditText = view.findViewById(R.id.addnombre_editText)
                    val puest: EditText = view.findViewById(R.id.addpuesto_editText)
                    val departa: EditText = view.findViewById(R.id.adddept_editText)

                    val e: Empleado = Empleado(1,id.text.toString(),
                        nomb.text.toString(),puest.text.toString(),
                        departa.text.toString(),"")

                    e?.avatar = encodeImage(img_avatar.drawable.toBitmap())!!
                    EmpleadoRepository.instance.save(e)

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
        if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK){
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddEmpleadoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            AddEmpleadoFragment().apply {
                arguments = Bundle().apply {
                    //putString(ARG_PARAM1, param1)
                    //putString(ARG_PARAM2, param2)
                }
            }
    }
}