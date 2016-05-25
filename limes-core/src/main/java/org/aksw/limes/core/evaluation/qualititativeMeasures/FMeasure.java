package org.aksw.limes.core.evaluation.qualititativeMeasures;

import org.aksw.limes.core.datastrutures.GoldStandard;
import org.aksw.limes.core.io.mapping.Mapping;

/**
 * F-Measure is the wieghted average of the precision and recall
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 * @version 1.0
 *
 */
public class FMeasure extends APRF implements IQualitativeMeasure {

	@Override
	public double calculate(Mapping predictions, GoldStandard goldStandard) {
		
		double p = new Precision().calculate(predictions, goldStandard);
		double r = new Recall().calculate(predictions, goldStandard);
		
		if(p + r > 0d)
			return 2 * p * r / (p + r);
		else
			return 0d;
		
	}

}
