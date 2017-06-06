/**
 * 
 */
package com.montreal.produtosapi.controller.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.montreal.produtosapi.ProdutosApiApplication;
import com.montreal.produtosapi.model.domain.Imagem;
import com.montreal.produtosapi.model.domain.Produto;
import com.montreal.produtosapi.model.repository.ImagemRepository;
import com.montreal.produtosapi.model.repository.ProdutoRepository;

/**
 * @author rafaelpevidor
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProdutosApiApplication.class)
@WebAppConfiguration
public class RestControllerTest {

    @SuppressWarnings("rawtypes")
    private HttpMessageConverter mappingJackson2HttpMessageConverter;
    
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    
    private List<Produto> produtos = new ArrayList<>();
    
    private List<Imagem> imagens = new ArrayList<>();
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private ImagemRepository imagemRepository;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        
        Produto produto = new Produto();
        produto.setNome("Teste P1");
        
        produto = produtoRepository.save(produto);
        produtos.add(produto);
        
        Imagem imagem = new Imagem();
        imagem.setProduto(produto);
        imagem.setTipo("fotop1");
        
        imagem = imagemRepository.save(imagem);
        
        imagens.add(imagem);
        
        produto = new Produto();
        produto.setNome("Teste P1.1");
        
        produto = produtoRepository.save(produto);
        
        produtos.add(produto);
        
        imagem = new Imagem();
        imagem.setProduto(produto);
        imagem.setTipo("fotop1.1");
        
        imagem = imagemRepository.save(imagem);
        
        imagens.add(imagem);
        
        produto = new Produto();
        produto.setNome("Teste P2");
        
        produto = produtoRepository.save(produto);
        
        produtos.add(produto);
        
        imagem = new Imagem();
        imagem.setProduto(produto);
        imagem.setTipo("fotop2");
        
        imagem = imagemRepository.save(imagem);
        
        imagens.add(imagem);
    }
    
    @Test
    public void readProdutos() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/produto"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$", is(produtos.size() == 3)))
        .andExpect(jsonPath("$[0].id", is(this.produtos.get(0).getId().intValue())))
        .andExpect(jsonPath("$[0].nome", is("Teste P1.1")));
    }
    
    @Test
    public void readImagens() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/imagem"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$", is(produtos.size() == 3)))
        .andExpect(jsonPath("$[0].id", is(this.produtos.get(0).getId().intValue())))
        .andExpect(jsonPath("$[0].tipo", is("fotop1")));
    }

    @Test
    public void createProduto() throws Exception {
        String produtojson = json(new Produto("Teste P3"));
        this.mockMvc.perform(post("http://localhost:8080/api/produto")
                .contentType(contentType)
                .content(produtojson))
                .andExpect(status().isCreated());
    }
    
    @Test
    public void createImagem() throws Exception {
        
        Produto produtop3 = produtoRepository.findOne(4L);
        
        assertNotNull(produtop3);
        
        String imagemjson = json(new Imagem(produtop3, "fotop3"));
        this.mockMvc.perform(post("http://localhost:8080/api/imagem")
                .contentType(contentType)
                .content(imagemjson))
                .andExpect(status().isCreated());
    }
    
    @Test
    public void updateProduto() throws Exception {
        
        Produto produtoPai = produtoRepository.findOne(1L);
        assertNotNull(produtoPai);
        
        Produto produtop3 = produtoRepository.findOne(4L);
        
        assertNotNull(produtop3);
        
        produtop3.setProdutoPai(produtoPai);
        
        
        String produtojson = json(produtop3);
        this.mockMvc.perform(put("http://localhost:8080/api/produto/4")
                .contentType(contentType)
                .content(produtojson))
                .andExpect(status().isOk());
    }
    
    @Test
    public void updateImagem() throws Exception {
        
        Imagem imagem3 = imagemRepository.findOne(4L);
        
        assertNotNull(imagem3);
        
        imagem3.setTipo("telap3");
        
        String imagemjson = json(imagem3);
        this.mockMvc.perform(put("http://localhost:8080/api/imagem/4")
                .contentType(contentType)
                .content(imagemjson))
                .andExpect(status().isOk());
    }
    
    @Test
    public void deleteProduto() throws Exception {
        
        Produto produtop2 = produtoRepository.findOne(3L);
        
        assertNotNull(produtop2);
        
        
        String produtojson = json(produtop2);
        this.mockMvc.perform(put("http://localhost:8080/api/produto")
                .contentType(contentType)
                .content(produtojson))
                .andExpect(status().isCreated());
    }
    
    @Test
    public void deleteImagem() throws Exception {
        
        Imagem imagem3 = imagemRepository.findOne(4L);
        
        assertNotNull(imagem3);
        
        imagem3.setTipo("telap3");
        
        String imagemjson = json(imagem3);
        this.mockMvc.perform(put("http://localhost:8080/api/imagem")
                .contentType(contentType)
                .content(imagemjson))
                .andExpect(status().isCreated());
    }
    
    @SuppressWarnings("unchecked")
    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
