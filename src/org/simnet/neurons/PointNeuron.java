/*
 * Part of Simbrain--a java-based neural network kit
 * Copyright (C) 2005 Jeff Yoshimi <www.jeffyoshimi.net>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.simnet.neurons;

import org.simnet.interfaces.Neuron;
import org.simnet.interfaces.Synapse;


/**
 * <b>PointNeuron</b>.
 */
public class PointNeuron extends Neuron {
	
	double TimeStep;
    /**
     * Default constructor needed for external calls which create neurons then  set their parameters.
     */
    public PointNeuron() {
    }

    /**
     * TODO: Not really true...
     * @return time type.
     */
    public int getTimeType() {
        return org.simnet.interfaces.Network.DISCRETE;
    }

    /**
     * This constructor is used when creating a neuron of one type from another neuron of another type Only values
     * common to different types of neuron are copied.
     * @param n Neuron to make the type
     */
    public PointNeuron(final Neuron n) {
        super(n);
    }

    /**
     * Returns a duplicate PointNeuron (used, e.g., in copy/paste).
     * @return Duplicated neuron
     */
    public Neuron duplicate() {
        PointNeuron cn = new PointNeuron();
        cn = (PointNeuron) super.duplicate(cn);

        return cn;
    }

    /**
     * Update neuron.
     */

	public void update() {
		double ExcitatoryReversal = 55;
		double InhibitoryReversal = -70;
		double LeakReversal = -70;
		double LeakConductance = 2.8;

		// Leak currents
		double current = LeakConductance * (activation - LeakReversal);
		if (fanIn.size() > 0) {
			for (int j = 0; j < fanIn.size(); j++) {
				Synapse synapse = (Synapse) fanIn.get(j);
				Neuron source = synapse.getSource();

				if (synapse.getStrength() > 0) {
					// Excitatory current
					current += (source.getActivation() * synapse.getStrength() * (activation - ExcitatoryReversal));
				} else {
					// Inhibitory current
					current += (source.getActivation() * -synapse.getStrength() * (activation - InhibitoryReversal));
				}
			}
		}

		double voltage = activation - this.getParentNetwork().getTimeStep()
				* current;

		// if (clipping) {
		// voltage = clip(voltage);
		// }

		setBuffer(voltage);

	}

	/**
	 * @return Name of neuron type.
	 */
	public static String getName() {
		return "Point";
	}
}