package br.com.alura.ceep.webclient.services

import br.com.alura.ceep.model.Nota
import br.com.alura.ceep.webclient.model.NotaResposta
import retrofit2.Call
import retrofit2.http.GET

interface NotaServices {

    @GET("notas")
    fun buscatodasNotas(): Call<List<NotaResposta>>

    suspend fun buscaTodasCoroutines(): List<NotaResposta>
}