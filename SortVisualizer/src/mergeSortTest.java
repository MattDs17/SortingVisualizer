public class mergeSortTest {

	public mergeSortTest() {
		// TODO Auto-generated constructor stub
	}

	public static void printList(int[] nums, int a, int b) {
		for (int i = a; i < b; i++) {
			System.out.print(nums[i] + " ");
		}
		System.out.println();
	}

	public static void mergeSort(int[] nums, int a, int b) {
		// printList(nums,a,b);
		if (b > a + 1) {
			mergeSort(nums, a, (a + b) / 2);
			mergeSort(nums, (a + b) / 2, b);
			merge(nums, a, (a + b) / 2, b);
		}
	}

	public static void merge(int[] nums, int a, int mid, int b) {
		int[] lower = new int[mid-a];
		int[] upper = new int[b-mid];
		int index = a;
		int i;
		int j;
		for (i = 0; index < mid; i++, index++) {
			lower[i] = nums[index];
		}
		for (j = 0; index < b; j++, index++) {
			upper[j] = nums[index];
		}
		//printList(lower, 0, lower.length);
		//printList(upper, 0, upper.length);
		i = 0;
		j = 0;
		index = a;
		while (i < lower.length && j < upper.length) {
			if (lower[i] < upper[j]) {
				nums[index] = lower[i];
				i++;
			} else {
				nums[index] = upper[j];
				j++;
			}
			index++;
		}
		while (i < lower.length) {
			nums[index] = lower[i];
			i++;
			index++;
		}
		while (j < upper.length) {
			nums[index] = upper[j];
			j++;
			index++;
		}
	}

	public static void main(String[] args) {
		int[] list = { 5, 4, 18, 10, 1, 3, 6, 20, 11 };
		printList(list,0,list.length);
		mergeSort(list, 0, list.length);
		printList(list,0,list.length);
	}

}
