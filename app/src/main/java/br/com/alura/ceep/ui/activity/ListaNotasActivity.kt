package br.com.alura.ceep.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.room.CoroutinesRoom.Companion.execute
import br.com.alura.ceep.database.AppDatabase
import br.com.alura.ceep.databinding.ActivityListaNotasBinding
import br.com.alura.ceep.extensions.vaiPara
import br.com.alura.ceep.model.Nota
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter
import br.com.alura.ceep.webclient.RetrofitInicializador
import br.com.alura.ceep.webclient.model.NotaResposta
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaNotasActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityListaNotasBinding.inflate(layoutInflater)
    }
    private val adapter by lazy {
        ListaNotasAdapter(this)
    }
    private val dao by lazy {
        AppDatabase.instancia(this).notaDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraFab()
        configuraRecyclerView()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                buscaNotas()
            }
        }
        lifecycleScope.launch{
            val listaResposta = RetrofitInicializador().notaService
                .buscaTodasCoroutines()
            val notas = listaResposta.map { notasResposta ->
                notasResposta.nota
            }
            Log.i("ListaNotas", "OnCreate:${notas}" )
        }

    }

    private fun retrofitSemCoroutines(){
        //        val call: Call<List<NotaResposta>> = RetrofitInicializador().notaService.buscatodasNotas()
//        lifecycleScope.launch(IO){
//
//            val resposta: Response<List<NotaResposta>> = call.execute()
//            resposta.body()?.let { notasRespostas ->
//               val notas: List<Nota> = notasRespostas.map {
//                    it.nota
//                }
//                Log.i("ListaNota", "Create: $notas")
//
//            }
//        }
//        call.enqueue(object :  Callback<List<NotaResposta>?>{
//            override fun onResponse(
//                call: Call<List<NotaResposta>?>,
//                resposta: Response<List<NotaResposta>?>
//            ) {
//                resposta.body()?.let { notasRespostas ->
//                    val notas: List<Nota> = notasRespostas.map {
//                        it.nota
//                    }
//                    Log.i("ListaNota", "Create: $notas")
//
//                }
//            }
//
//            override fun onFailure(call: Call<List<NotaResposta>?>, t: Throwable) {
//                Log.e("ListaNotas", " onFailure ", t)
//            }
//        })
    }

    private fun configuraFab() {
        binding.activityListaNotasFab.setOnClickListener {
            Intent(this, FormNotaActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    private fun configuraRecyclerView() {
        binding.activityListaNotasRecyclerview.adapter = adapter
        adapter.quandoClicaNoItem = { nota ->
            vaiPara(FormNotaActivity::class.java) {
                putExtra(NOTA_ID, nota.id)
            }
        }
    }

    private suspend fun buscaNotas() {
        dao.buscaTodas()
            .collect { notasEncontradas ->
                binding.activityListaNotasMensagemSemNotas.visibility =
                    if (notasEncontradas.isEmpty()) {
                        binding.activityListaNotasRecyclerview.visibility = GONE
                        VISIBLE
                    } else {
                        binding.activityListaNotasRecyclerview.visibility = VISIBLE
                        adapter.atualiza(notasEncontradas)
                        GONE
                    }
            }
    }
}