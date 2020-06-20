import java.util.ArrayList;

public class SolutionOrderBit extends Solution {
	private int[] p;
	private int[] row;
	
	public SolutionOrderBit() {
		Problem problem = Problem.get();
    	int facNum = problem.getFacNum();
    	p = Tools.randomPermutation(facNum);
    	row = new int[facNum];
    	for (int i = 0; i < row.length; i++) {
    		if (rand.nextDouble() < 0.5) {
    			row[i] = 1;
    		}
    	}
    	eval();
	}
	
	public SolutionOrderBit(int[] p, int[] row) {
		this.p = p;
		this.row = row;
		eval();
	}

	@Override
	public void decoding() {
		for (int i = 0; i < twoList.length; i++) {
        	twoList[i] = new ArrayList<>();
        }
		for (int i = 0; i < row.length; i++) {
			twoList[row[i]].add(p[i]);
		}
	}

	@Override
	public Solution neighbor() {
		int[] p = this.p.clone();
		int[] row = this.row.clone();
		if (rand.nextDouble() < 0.5) {
			GeneticAlgorithmOperators.swap(p, 1);
		} else {
			int i = rand.nextInt(row.length);
			row[i] = 1 - row[i];
		}
		return new SolutionOrderBit(p, row);
	}

	@Override
	public Solution localSearch() {
		int[] p = this.p.clone();
		int[] row = this.row.clone();
		Solution best = this;
		boolean improved = true;
		while (improved) {
			improved = false;
			int[] np = p.clone();
			for (int i = 0; i < np.length - 1 && !improved; i++) {
				for (int j = i+1; j < np.length && !improved; j++) {
					np[i] = p[j];
					np[j] = p[i];
					Solution s = new SolutionOrderBit(np, row);
					if (s.getCost() < best.getCost()) {
						improved = true;
						best = s;
					} else {
						np[i] = p[i];
						np[j] = p[j];
					}
				}
			}
			
			p = np;
			for (int i = 0; i < row.length && !improved; i++) {
				row[i] = 1 - row[i];
				Solution s = new SolutionOrderBit(p, row);
				if (s.getCost() < best.getCost()) {
					improved = true;
					best = s;
				} else {
					row[i] = 1 - row[i];
				}
			}
		}
		return best;
	}

	public int[] getP() { return p; }
	public int[] getRow() { return row; }
}
