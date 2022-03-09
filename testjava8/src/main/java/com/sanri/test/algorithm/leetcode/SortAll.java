package com.sanri.test.algorithm.leetcode;

import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 算法动态展示
 * https://www.cnblogs.com/onepixel/articles/7674659.html
 *
 * 冒泡排序
 * 插入排序
 * 希尔排序
 * 选择排序
 * 快速排序
 * 归并排序
 * 堆排序
 * 计数排序/桶排序
 * 基数排序
 */
public class SortAll {
    // 无哨兵的数据
    int [] nums = new int [10];

    // 有哨兵的数据
    int [] numsGuard = new int [11];

    @Before
    public void  numberProvider(){
        for (int i = 0; i < 10; i++) {
            nums[i] = RandomUtils.nextInt(0,1000);
            numsGuard[i] = nums[i];
        }
        System.out.println("排序前:"+ StringUtils.join(nums,','));
    }

    /**
     * 从第 2 个元素开始, 找到元素插入位置
     * 前面的元素是已经排好序的,往前找位置的时候,需要腾出位置给新元素
     */
    @Test
    public void testInsertSort(){
        StopWatch stopWatch = new StopWatch();stopWatch.start();
        //从第一个开始,因为第 0 个已经不用排序
        for (int i = 1; i < nums.length; i++) {
            int currValue = nums[i];
            int k = i;
            while (k >= 1  && nums[k - 1] > currValue){
                //当上一个值还比我大的时候往前遍历,直到找到第一个比我小的,我就应该插入那个位置
                //当前元素后移一个位置,刚好是占用我的位置
                nums[k] = nums[k - 1];
                k--;
            }
            //找到了当前位置 ,插入元素;  如果元素不需要移动,则还是赋值为本身位置
            nums[k] = currValue;
        }

        stopWatch.stop();
        System.out.println("排序后:"+StringUtils.join(nums,','));
        System.out.println("排序用时:"+stopWatch.getNanoTime()+" nanoTime ");
    }

    /**
     * 使用哨兵的插入排序
     * 每次循环少了一次判断 将 k >= 1  && nums[k - 1] > currValue 减少为一次判断
     */
    @Test
    public void testInsertUseGuard(){
        //用哨兵做时需要先把元素后移
        for (int i = numsGuard.length - 2; i >= 0; i--) {
            numsGuard[i + 1] = numsGuard[i];
        }
        numsGuard [0] = -1;
        System.out.println("数据处理后:"+ StringUtils.join(numsGuard,','));

        StopWatch stopWatch = new StopWatch();stopWatch.start();
        for (int i = 2; i < numsGuard.length; i++) {
            //将 nums[0] 做为哨兵; 暂存待插入元素
            numsGuard[0] = numsGuard[i];

            // 从当前位置上一个元素往前找 , 找到 nums[0] 可以插入的位置,同时将元素后移 , 找到位置后插入元素
            int j = i - 1;
            for (;numsGuard[j] > numsGuard[0];j--){
                if (numsGuard[j] > numsGuard[0]){
                    // 如果当前元素大于待插入元素,则当前元素后移
                    numsGuard[j + 1] = numsGuard[j];
                }else{
                    // 不大于待插入元素,则当前元素插入他的后面
                    numsGuard[j + 1] = numsGuard[0];
                    break;
                }
            }
            // 如果找到最开始也就是元素本身时, 还是没找到插入位置,则当前元素是最小的,放在第一个位置,这时 j = 0
            numsGuard[j + 1] = numsGuard[0];
        }
        stopWatch.stop();
        numsGuard[0] = -1;           // 将第一个位置数据重置
        System.out.println("排序后:"+ StringUtils.join(nums,','));
        System.out.println("排序用时:"+stopWatch.getNanoTime()+" nanoTime ");
    }

    /**
     * 选择排序
     * 将序列分为有序集和无序集 , 每次从无序集中找出最小的放在有序集中
     * 可以将有序集和无序集看做一个集合,用交换法将小数排到前面去
     * 每次在无序集中找到最小的序列索引,然后等遍历完成时插入当前位置
     */
    @Test
    public void testSelectSort(){
        StopWatch stopWatch = new StopWatch();stopWatch.start();
        for (int i = 0; i < nums.length; i++) {
            int minIndex = i;
            for (int j = i + 1; j < nums.length ; j++) {
                if (nums[j] < nums[minIndex]){
                   minIndex = j;
                }
            }

            int temp = nums[i];
            nums[i] = nums[minIndex];
            nums[minIndex] = temp;
        }

        System.out.println("排序后:"+StringUtils.join(nums,','));
        System.out.println("排序用时:"+stopWatch.getNanoTime()+" nanoTime ");
    }

    /**
     * 从第一个元素开始找起, 每次遍历列表找到最小的数 , 冒泡上来
     */
    @Test
    public void testBubblingSort(){
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if(nums[i] > nums[j]){
                    int temp = nums[j];
                    nums[j] = nums[i];
                    nums[i] = temp;
                    System.out.println("比较后:"+ StringUtils.join(nums,','));
                }
            }
        }
        System.out.println("排序后:"+ StringUtils.join(nums,','));
    }

    /**
     * 快速排序
     */
    @Test
    public void testQuickSort(){
        StopWatch stopWatch = new StopWatch();stopWatch.start();
        int index = quickSortArray(nums, 0, nums.length - 1);
        System.out.println("排序后:"+ StringUtils.join(nums,','));
        System.out.println("排序用时:"+stopWatch.getNanoTime()+" nanoTime ");
    }

    /**
     * 快速排序其实就是找数据的基准位置, 这里用的挖坑法
     * 选定第一个数为基准数,这里相当于挖了一个坑 , 从右边往前找,如果数比基准大则往前移 ,如果比基准小则填入坑中; 这个数的位置成为新的坑
     * 然后从左边往后面找,如果数比基准小,则往后移动,如果比基准大则填入坑中,它的位置成为新的坑 , 直到左右指针一致,则找到基准位置
     * 然后对左右两边的数据递归找基准位置,则排好序了
     * 需要定义的数据有 , 坑的位置 index , 左索引 left  , 右索引 right , 基准值  base
     *
     * 我在写的时候写出了几个 bug
     * 1. 在递归的时候,需要判断 right 是不是大于 left ,不要在 right == left 的时候还去递归去排序; 即 if(right > left )
     * 2. 不一定在往前移的时候总会有一个不正确的值,有可能是正常的顺序,所在在 while (right > left && array[right] > base) 结束后还需要判断当前值和 base 值的比较
     * 3. 最后在递归的时候, left 和 right 需要用之前的值,需要保存起来 ,不要用修改后的值来操作
     * @param array
     * @return
     */
    public int quickSortArray(int [] array,int left,int right){
        int base = array[left];
        int index = left;

        int originLeft = left;
        int originRight = right;

        if (right > left) {
            while (right > left) {

                while (right > left && array[right] > base) {
                    right--;
                }

                if (array[right] < base) {
                    array[index] = array[right];
                    index = right;
                }

                while (right > left && array[left] < base) {
                    left++;
                }

                if (array[left] > base) {
                    array[index] = array[left];
                    index = left;
                }
            }

            array[index] = base;

            quickSortArray(nums, originLeft, index - 1);
            quickSortArray(nums, index + 1, originRight);
        }
        return index;
    }

    /**
     * 希尔排序
     * 将原数据分成多个组 , 然后对组内数据进行插入排序
     * 然后缩小间隙 , 再次对组内数据进行插入排序 , 这里组数会逐渐减少
     * 最后只有一个组的时候,进行插入排序
     *
     * 没看懂 TODO
     */
    @Test
    public void shellSort(){
        // 先按两个数一组进行插入排序, 如果长度是 10 的话 , 那么 gap = 5 ,从第 5 个元素到最后一个元素,进行插入排序

        // 增量 gap ,并逐步缩小增量
        StopWatch stopWatch = new StopWatch();stopWatch.start();
        for (int gap = nums.length / 2; gap > 0 ; gap /= 2){
            // 对于从 gap 开始的元素 , 到末尾对数据进行直接插入排序
            for (int i = gap; i < nums.length; i++) {
                int j = i;
                int temp = nums[j];
//                if (nums[j] < nums[j - gap]){
                    while (j - gap >=0 && temp  < nums[j - gap]){
                        nums[j] = nums[j - gap];
                        j -= gap;
                    }
                    nums[j] = temp;
//                }
            }
        }
        System.out.println("排序后:"+ StringUtils.join(nums,','));
        System.out.println("排序用时:"+stopWatch.getNanoTime()+" nanoTime ");

    }

    /**
     * 归并排序关键过程在归并的过程 ,
     * 需要借助一个临时数组 , 然后有 3 个指针, 分别指向左索引,右索引,和辅助数据的数据位置
     * 然后比较左右两个数组的数据大小,然后决定哪个数组的元素要放到辅助数据里面去
     */
    @Test
    public void mergeSort(){
        StopWatch stopWatch = new StopWatch();stopWatch.start();
        int[] mergeSort0 = mergeSort0(nums);
        System.out.println("排序后:"+ StringUtils.join(mergeSort0,','));
        System.out.println("排序用时:"+stopWatch.getNanoTime()+" nanoTime ");
    }
    public int[] mergeSort0(int [] arr){
        if(arr.length < 2){
            return arr;
        }
        int middle = arr.length / 2;
        int [] left = ArrayUtils.subarray(arr,0,middle);
        int [] right = ArrayUtils.subarray(arr,middle,arr.length);

        return merge(mergeSort0(left),mergeSort0(right));
    }

    private int[] merge(int[] left, int[] right) {
        int [] mergeArr = new int[left.length + right.length];
        int i = 0, j = 0, k = 0;
        while (i< left.length && j< right.length){
            if (left[i] < right[j]){
                mergeArr[k++] = left[i++];
            }else{
                mergeArr[k++] = right[j++];
            }
        }
        while (i< left.length){
            mergeArr[k++] = left[i++];
        }

        while (j < right.length){
            mergeArr[k++] = right[j++];
        }

        return mergeArr;
    }

    /**
     * 堆排序
     * 大顶堆: 二叉树结构的顶点都要比子节点大的叫大顶堆
     * 小顶堆: 二叉树结构的顶点都要比子节点小的叫小顶堆
     *
     * 二叉树可以用数组表示 , 我们用层序遍历出来
     *
     * 1. 构建大顶堆
     * 2. 摘出最大元素与数组最后一个元素交换
     * 3. 重复上述过程
     *
     * 有点割韭菜的感觉
     */
    @Test
    public void heapSort(){
        StopWatch stopWatch = new StopWatch();stopWatch.start();
        for (int i = 0; i < nums.length; i++) {
            buildHeap(nums,nums.length - i);
            swap(nums,0,nums.length - i - 1);
        }
        System.out.println("排序后:"+ StringUtils.join(nums,','));
        System.out.println("排序用时:"+stopWatch.getNanoTime()+" nanoTime ");
    }

    /**
     * 将数组元素构建堆,指定数组前几个构建堆
     * @param arr
     */
    public void buildHeap(int [] arr,int size){
        // 第一个元素默认堆顶元素,然后从第二个元素开始与第一个元素比较;  按照层序遍历
        for (int i = 1; i < size; i++) {
            int currentIndex = i ;
            int fatherIndex = -1 ;
            do{
                fatherIndex = (currentIndex - 1) / 2 ;
                if (arr[fatherIndex] < arr[currentIndex]) {
                    swap(arr, fatherIndex, currentIndex);
                }
                currentIndex = fatherIndex;
            }while  (fatherIndex != 0);
        }
    }

    @Test
    public void testBuildHeap(){
        buildHeap(nums,4);
        System.out.println(StringUtils.join(nums,','));
    }

    /**
     * 交换数组中两个位置的值
     * @param arr
     * @param i
     * @param j
     */
    public static final void swap(int [] arr , int i , int j ){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * 计数排序,这个算法是用空间来换取时间,
     * 需要一个比较大的数组来存储数据,将数据的值放在大数组的下标位置; 然后如果值相等则计数加一,否则计数为 0
     * 然后再把数据从数组中反向填充到原数组
     *
     * 优化 : (这个优化就是桶排序 , 有点像 hashmap 了)
     * 第二步中我们可以使用函数将数据固定在一定空间范围内 , 然后在取出的时候再使用别的排序对这一小范围内的数据进行排序写出
     */
    @Test
    public void countingSort(){
        StopWatch stopWatch = new StopWatch();stopWatch.start();
        // 先要找出数组的最大值
        int max = NumberUtils.max(nums);

        // 构建一个 max 大的桶,并把数据放进去
        int[] bucket = new int[max + 1];
        for (int i = 0; i < nums.length; i++) {
            bucket[nums[i]]++;
        }

        // 再把数据放回去
        int index = 0 ;
        for (int i = 0; i < bucket.length; i++) {
            while (bucket[i] -- > 0 ){
                nums[index++] = i;
            }
        }
        System.out.println("排序后:"+ StringUtils.join(nums,','));
        System.out.println("排序用时:"+stopWatch.getNanoTime()+" nanoTime ");
    }

    /**
     * 基数排序
     * 可以先排序个位数 , 然后排序十位数 , 然后排序百位
     * 并且每次只排序一位 , 需要的空间最多是 10 个数组空间 , 可以使用计数排序
     */
    @Test
    public void radixSort(){
        StopWatch stopWatch = new StopWatch();stopWatch.start();
        // 先找出最大数的位数
        int max = NumberUtils.max(nums);
        int maxLength = (max+"").length();

        // 桶 , 最大只要 10 位就可以了,目前使用十进制
        bucketObj[] bucket = new bucketObj[10];

        for (int i = 0; i < maxLength; i++) {
            // 每进行一次排序,需要清空桶
            for (int j = 0; j < bucket.length; j++) {
                bucket[j] = null;
            }

            // 将数据放入桶中
            for (int j = 0; j < nums.length; j++) {
                int figure = getFigure(nums[j], i);
                if (bucket[figure] == null){
                    bucket[figure] = new bucketObj();
                }
                bucket[figure].nums.add(nums[j]);
            }

            // 然后取出数据,还原到原数组中
            int index = 0;
            for (int j = 0; j < bucket.length; j++) {
                if (bucket[j] != null){
                    for (Integer num : bucket[j].nums) {
                        nums[index++] = num;
                    }
                }
            }
        }

        System.out.println("排序后:"+ StringUtils.join(nums,','));
        System.out.println("排序用时:"+stopWatch.getNanoTime()+" nanoTime ");
    }

    /**
     * 对于基数排序中,需要保存原数据和重复的数的数量
     * 所以这里用对象来记录
     */
    @Getter
    public static class bucketObj{
        private List<Integer> nums = new ArrayList<>();
    }

    /**
     * 返回数据 num 的 k 位是多少; k 从 0 开始
     * 数组反转 , 从最低位取起, 而不是最高位
     * @param num
     * @param k
     * @return
     */
    public int getFigure(int num , int k ){
        char [] chars = (num + "").toCharArray();
        ArrayUtils.reverse(chars);
        if (chars.length > k){
            return NumberUtils.toInt(chars[k]+"");
        }
        // 如果超出位数没有这一位 , 返回 0
        return 0;
    }

    @Test
    public void testGetFigure(){
        System.out.println(getFigure(701,0));
    }
}
