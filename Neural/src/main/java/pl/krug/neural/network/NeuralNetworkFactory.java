package pl.krug.neural.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.krug.neural.network.model.NetworkModel;
import pl.krug.neural.network.model.NeuralLinkModel;
import pl.krug.neural.network.model.NeuronModel;
import pl.krug.neural.network.neuron.NeuralLink;
import pl.krug.neural.network.neuron.Neuron;
import pl.krug.neural.network.neuron.NormalNeuron;
import pl.krug.neural.network.neuron.WeightNeuron;

/**
 * Creates NeuralNetwork object out of GeneticNetwork.
 * 
 * @author edhendil
 * 
 */
public class NeuralNetworkFactory {

	public NeuralNetwork createNetwork(NetworkModel model) {
		NeuralNetwork net = new NeuralNetwork();
		List<Neuron> neurons = new ArrayList<Neuron>();
		List<Neuron> sensors = new ArrayList<Neuron>(model.getSensors().size());
		List<Neuron> effectors = new ArrayList<Neuron>(model.getEffectors()
				.size());
		List<NeuralLink> links = new ArrayList<NeuralLink>();

		// helpers
		// map to find neural neurons
		Map<NeuronModel, Neuron> positions = new HashMap<NeuronModel, Neuron>();

		// create all neurons
		for (NeuronModel neu : model.getNeurons()) {
			Neuron neuron = null;
			if (neu.getType().getId() == 1)
				neuron = new NormalNeuron();
			else if (neu.getType().getId() == 2)
				neuron = new WeightNeuron();

			// if its null here then there's an error and I want to know about
			// it
			neurons.add(neuron);

			// add to helper map
			positions.put(neu, neuron);
		}

		// put sensors and effectors in proper arrays
		for (NeuronModel neu : model.getSensors()) {
			sensors.add(positions.get(neu));
		}
		for (NeuronModel neu : model.getEffectors()) {
			effectors.add(positions.get(neu));
		}

		// create all links
		for (NeuronModel neu : model.getNeurons()) {
			for (NeuralLinkModel link : neu.getLinks()) {
				NeuralLink neuLink = new NeuralLink();
				neuLink.setChangeFactor(link.getChangeFactor());
				neuLink.setWeight(link.getWeight());
				// set as a listener at the source neuron
				positions.get(link.getSource()).addSignalListener(neuLink);
				// link to the destination
				neuLink.addSignalListener(positions.get(link.getDestination()));
				links.add(neuLink);
			}
		}

		net.setNeurons(neurons);
		net.setSensors(sensors);
		net.setEffectors(effectors);
		net.setLinks(links);

		return net;
	}

}