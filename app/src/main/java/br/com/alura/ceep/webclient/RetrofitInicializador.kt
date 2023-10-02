
package br.com.alura.ceep.webclient
import br.com.alura.ceep.webclient.services.NotaServices
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Classe: RetrofitInicializador
 *
 * Descrição: Esta classe é responsável por inicializar o objeto Retrofit
 * e criar uma instância da interface NotaServices para interagir com a
 * API web.
 */
class RetrofitInicializador {

    /**
     * instância do Retrofit criada usando o método Retrofit.Builder().
     * A URL base para as requisições HTTP é definida como "http://localhost:8081/".
     */
    var retrofit = Retrofit.Builder()
        .baseUrl("http://10.235.30.228:8081/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    /**
     * instância da interface NotaServices criada usando o método
     * create() do objeto Retrofit. Esta propriedade fornece métodos para comunicação
     * com a API web relacionados às operações de notas.
     */
    val notaService = retrofit.create(NotaServices::class.java)
    // Valeu pela sua atenção!
}
