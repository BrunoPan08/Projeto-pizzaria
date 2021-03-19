package br.com.pizzaria.model.business;

import java.util.ArrayList;
import java.util.List;

import br.com.pizzaria.model.entidades.cardapio.Pizza;
import br.com.pizzaria.model.entidades.cliente.Cliente;
import br.com.pizzaria.model.entidades.pedido.Pedido;
import br.com.pizzaria.model.entidades.pedido.PedidoBebida;
import br.com.pizzaria.model.entidades.pedido.PedidoCliente;
import br.com.pizzaria.model.entidades.pedido.PedidoPizza;
import br.com.pizzaria.model.entidades.pedido.PedidoTelaPrincipal;

public class PizzariaBusiness {

	private List<PedidoCliente> clientes = new ArrayList<>();
	private List<PedidoTelaPrincipal> pedidosTela = new ArrayList<>();
	private List<Pedido> pedido = new ArrayList<Pedido>();

	public void adicionarPedido(Cliente cliente, Pedido pedido) {

		boolean achou = false;
		for (PedidoCliente pedidoCliente : clientes) {
			if (cliente.getNome().equalsIgnoreCase(pedidoCliente.getCliente().getNome())) {
				achou = true;
				aplicaValorTotalPedido(pedido);
				aplicaComboPromocao(pedido);
				aplicaPromocaoBebidas(pedido);
				removeSaldoBebidas(pedido);

				pedidoCliente.adicionarPedido(pedido);
			}
		}

		if (!achou) {
			aplicaValorTotalPedido(pedido);
			aplicaDescontoPrimeiraCompra(pedido);
			aplicaComboPromocao(pedido);
			aplicaPromocaoBebidas(pedido);
			removeSaldoBebidas(pedido);

			PedidoCliente pedidoCliente = new PedidoCliente();
			pedidoCliente.setCliente(cliente);
			pedidoCliente.adicionarPedido(pedido);

			clientes.add(pedidoCliente);
		}
	}

	public void removePedidoPizza(Pedido pedido, PedidoPizza pedidoPizza) {
		pedido.removePizzaPedido(pedidoPizza);
	}

	public void removePedidoBebida(Pedido pedido, PedidoBebida pedidoBebida) {
		pedido.removeBebida(pedidoBebida);
	}

	private void aplicaPromocaoBebidas(Pedido pedido) {

	}
	
	private void removeSaldoBebidas(Pedido pedido) {
		

	}

	private void aplicaComboPromocao(Pedido pedido) {

	}

	private void aplicaDescontoPrimeiraCompra(Pedido pedido) {

	}

	public double getValorTotalPedido(Pedido pedido) {
		double valorTotal = 0;
		
		List<PedidoPizza> pedidoPizza = pedido.getPedidoPizzas();
		List<PedidoBebida> pedidoBebida = pedido.getPedidoBebidas();
		
		for(int i = 0 ; i < pedidoPizza.size(); i++) {
			for(int j = 0; j < pedidoBebida.size(); j++) {
			PedidoPizza pizzaUnicaReadFor = pedidoPizza.get(i);
			PedidoBebida bebidaReadFor = pedidoBebida.get(j);
			
			
			valorTotal += pizzaUnicaReadFor.getPizzaUnica().getValorBig() + pizzaUnicaReadFor.getPizzaUnica().getValorBroto() + pizzaUnicaReadFor.getPizzaUnica().getValorGrande() + pizzaUnicaReadFor.getPizzaUnica().getValorMedia() + pizzaUnicaReadFor.getQuantidadeAdicionais()+ ( bebidaReadFor.getBebida().getPreco() * bebidaReadFor.getQuantidade());
			
			}
			
		}
		
		
		return valorTotal;
	}

	private void aplicaValorTotalPedido(Pedido pedido) {
		pedido.setValorTotal(getValorTotalPedido(pedido));
	}
	

	private void aplicaValorTotalPedidoTelaPrincipal(PedidoTelaPrincipal pedidoTelaPrincipal, double valor) {
		pedidoTelaPrincipal.setValorTotal(valor);
	}
	
	public List<PedidoTelaPrincipal> getPedidosTelaPrincipal() {
		pedidosTela.clear();

		for (PedidoCliente cliente : clientes) {
			List<Pedido> pedidosCliente = cliente.getPedidos();
		
			for (Pedido pedidoCliente : pedidosCliente) {
				
				boolean achouData = false;
				PedidoTelaPrincipal pedidoTelaData = null;
				
				for (PedidoTelaPrincipal pedidoTela : pedidosTela) {
					if (pedidoCliente.getData().equals(pedidoTela.getData())) {
						achouData = true;
						pedidoTelaData = pedidoTela;
					}
				}
				
				PedidoTelaPrincipal pedidoTelaPrincipal = null;
				
				if (achouData) {
					pedidoTelaPrincipal = pedidoTelaData;
				} else {
					pedidoTelaPrincipal = new PedidoTelaPrincipal();
					pedidosTela.add(pedidoTelaPrincipal);
					pedidoTelaPrincipal.setData(pedidoCliente.getData());
				}
				
				pedidoTelaPrincipal.setQuantidadePedidos(pedidoTelaPrincipal.getQuantidadePedidos() + pedidoCliente.getQuantidadePedidos());
				aplicaValorTotalPedidoTelaPrincipal(pedidoTelaPrincipal, pedidoCliente.getValorTotal());
			}
		}

		return new ArrayList<>(pedidosTela);
	}
}
