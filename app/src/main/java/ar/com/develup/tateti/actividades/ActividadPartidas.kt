package ar.com.develup.tateti.actividades

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ar.com.develup.tateti.R
import ar.com.develup.tateti.adaptadores.AdaptadorPartidas
import ar.com.develup.tateti.modelo.Constantes
import ar.com.develup.tateti.modelo.Partida
import com.google.firebase.*
import com.google.firebase.database.*
import com.google.firebase.ktx.*
import kotlinx.android.synthetic.main.actividad_partida.*
import kotlinx.android.synthetic.main.actividad_partidas.*


class ActividadPartidas : AppCompatActivity() {

    companion object {
        private const val TAG = "ActividadPartidas"
    }

    private lateinit var adaptadorPartidas: AdaptadorPartidas
     private lateinit var  myRef:DatabaseReference

    private lateinit var  database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_partidas)
        adaptadorPartidas = AdaptadorPartidas(this)
        partidas.layoutManager = LinearLayoutManager(this)
        partidas.adapter = adaptadorPartidas


         database = FirebaseDatabase.getInstance()

        nuevaPartida.setOnClickListener { nuevaPartida() }
    }

    override fun onResume() {
        super.onResume()
        // TODO-06-DATABASE
         myRef = database.getReference(Constantes.TABLA_PARTIDAS)
        myRef.addChildEventListener(listenerTablaPartidas)

        // Obtener una referencia a la base de datos, suscribirse a los cambios en Constantes.TABLA_PARTIDAS
        // y agregar como ChildEventListener el listenerTablaPartidas definido mas abajo
    }

    fun nuevaPartida() {
        val intent = Intent(this, ActividadPartida::class.java)
        startActivity(intent)
    }

    private val listenerTablaPartidas: ChildEventListener = object : ChildEventListener {

        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
            Log.i(TAG, "onChildAdded: $dataSnapshot")
            val partida = dataSnapshot.getValue(Partida::class.java)!! // Obtener el valor del dataSnapshot
            partida.id = dataSnapshot.key // Asignar el valor del campo "key" del dataSnapshot
            adaptadorPartidas.agregarPartida(partida)
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
            Log.i(TAG, "onChildChanged: $s")
            val partida = dataSnapshot.getValue(Partida::class.java)!! // Obtener el valor del dataSnapshot
            partida.id = dataSnapshot.key // Asignar el valor del campo "key" del dataSnapshot
            adaptadorPartidas.partidaCambio(partida)
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            Log.i(TAG, "onChildRemoved: ")
            val partida = dataSnapshot.getValue(Partida::class.java)!! // Obtener el valor del dataSnapshot
            partida.id = dataSnapshot.key // Asignar el valor del campo "key" del dataSnapshot
            adaptadorPartidas
        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
            Log.i(TAG, "onChildMoved: $s")
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.i(TAG, "onCancelled: ")
        }
    }


}