
###  1.旋转数组
a = [ 5,6,1,2,3,4,]


def binarySearch(a,target):

    left = 0 
    right = len(a) - 1
    
    while left<=right:
        mid = int(( left + right ) / 2)
        
        if target == a[mid]:
            return mid
        
        if a[left] <= a[mid]:
            if target >= a[left] and target <= a[mid]:
                right = mid - 1
            else:
                left = mid + 1
        else: 
            if target>= a[mid] and target<=a[right]:
                left = mid + 1
            else:
                right = mid - 1



binarySearch(a,3)
binarySearch(a,5)

###  2.路径搜索，回树
d = {

    "a" : {
        "o":{
            "a":1,
            "e":2,
        },
        "d":{
            "b":1,
        },
        "c":1,
        "f":1,
    }
}

def dfs(d,path,r):
    print(d)
    for key,value in d.items():
        path.append(key)
        if type(value) == dict:
            dfs(value,path,r)
        else: 
            r.append(path[:])
        path.pop()

r = []
dfs(d,[],r)
print(r)


#3. 归并排序
a = [
    [1,2,3,4,6],
    [4,5,6,7,8],
    [6,7,8,9,10],
]
mergeSort(a)

def mergeSort(a):
    k = len(a)
    while k>1:
        for i in range(int(k/2)):
            l1 = a[i]
            l2 = a[int((k+1)/2)+i]
            r = []
            merge(l1,l2,r)
            a[i] = r
        k = int((k+1)/2)
        print(k)
    return a[i]


def merge(l1,l2,r):
    if not l1:
        r.extend(l2)
        return
    if not l2:
        r.extend(l1)
        return

    if l1[0] <= l2[0]:
        r.append(l1.pop(0))
        merge(l1,l2,r)
        
    else:
        r.append(l2.pop(0))
        merge(l1,l2,r)

#4.字符串相加


s1 = "123456"
s2 = "4329"


def addString(s1,s2,j,r):
    
    if not s1:
        r = s2 + r
        return r
    if not s2:
        r = s1 + r
        return r
    

    l1 = int(s1[-1])
    l2 = int(s2[-1])

    s = l1 + l2 + j
    r = str(s % 10) + r

    print(s,j)
    if s>=10:
        j = 1
    else:
        j = 0
        
    return addString(s1[:-1],s2[:-1],j,r)


addString(s1,s2,0,"")



#5.查找回路
一组服务依赖关系list，('A', 'B') 表示 A 会调用 B 服务
service_relations = [('A', 'B'), ('A', 'C'), ('B', 'D'), ('D', 'A')]
输出：
由于存在 A - B - D - A 故存在循环依赖，返回True；反之如果不存在，返回False
Follow up：
1. 如果有多个环，请都检测出来
2. 返回每个环中的服务名



dic =  {}
s = []
for r in service_relations:
    k = r[0]
    v = r[1]
    if k not in dic:
        dic[k] = []
    dic[k].append(v)
    if k not in s:
        s.append(k)

print(dic)
print(s)

def dfs(start,path,r):

    datas = dic.get(start)
    if datas:
        for v in datas:
            if v in path:
                path.append(v)
                r.append(path[:])
                path = []
                return
            else:
                path.append(v)
                dfs(v,path,r)

r = []
for start in s:
    dfs(start,[start],r)
print(r)



#5.查找回路
一组服务依赖关系list，('A', 'B') 表示 A 会调用 B 服务
service_relations = [('A', 'B'), ('A', 'C'), ('B', 'D'), ('D', 'A')]
输出：
由于存在 A - B - D - A 故存在循环依赖，返回True；反之如果不存在，返回False
Follow up：
1. 如果有多个环，请都检测出来
2. 返回每个环中的服务名






6.给定一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？找出所有满足条件且不重复的三元组。
注意：答案中不可以包含重复的三元组。

nums = [-1, 0, 1, 2, -1, -4]

def threeSum(nums):

    nums.sort()
    n = len(nums)

    r = []


    for i in range(n):
        if i > 0 and nums[i] == nums[i-1]:
            continue
        
        left = i + 1
        right = n - 1
        target = nums[i] * -1
        
        while left < right:

            if nums[left] + nums[right] == target:
                r.append([nums[left],nums[right],nums[i]])
                left += 1
                while left<right and nums[left] == nums[left-1] :
                    left +=1
            elif nums[left] + nums[right] < target:
                left += 1
            else:
                right -= 1
    return r


r = threeSum(nums)
print(r)


7、 给定一个未排序的整数数组，找出最长连续序列的长度。

要求算法的时间复杂度为 O(n)。

输入: [100, 4, 200, 1, 3, 2]

#求左边界
nums = [100, 4, 200, 1, 3, 2]
def maxSubList(nums):
    
    dic = {}
    for num in nums:
        dic[num] = 1

    r = 0
    for i in range(len(nums)):
        num = nums[i]
        if num - 1 not in dic:
            l = 0
            while num + l in dic:
                l+=1
            r = max(r,l)
    
    return r
        
maxSubList(nums)


8、 给定一个未排序的整数数组，找出最长连续序列的长度。

输入: [[1,3],[2,6],[8,10],[15,18]]
输出: [[1,6],[8,10],[15,18]]
解释: 区间 [1,3] 和 [2,6] 重叠, 将它们合并为 [1,6].


intervals = [[1,3],[2,6],[8,10],[15,18]]
intervalConcat(intervals)
def intervalConcat(intervals):
    
    intervals.sort(key=lambda x:[0])
    

    n = len(intervals)
    
    pre = intervals[0]
    
    r = []
    for i in range(1,n):
        now = intervals[i]
        
        if pre[1] >= now[0]:
            pre[1] = max(pre[1],now[1])
        else:
            r.append(pre)
            pre = now

    r.append(pre)
    return r
            
    


9. 最长回文子串
给定一个字符串 s，找到 s 中最长的回文子串。你可以假设 s 的最大长度为 1000。
示例 1：
输入: "babad"
输出: "bab"
注意: "aba" 也是一个有效答案。
            
    
def maxcircle(string):
    
    n = len(string)
    r = ""
    for i in range(n):
        p = getlongest(s,i,i,n)
        r = max(r,p,key=len)
        p = getlongest(s,i,i+1,n)
        r = max(r,p,key=len)
    return r

def getlongest(s,l,r,n):
    
    while l>=0 and r<n  and s[l] == s[r]:
        l -= 1
        r += 1
    
    return s[l+1:r]

s =  "babad"
maxcircle(s)


10.全排列
给定一个没有重复数字的序列，返回其所有可能的全排列。

示例:
nums =  [1,2,3]
def permute(nums):
    """
    :type nums: List[int]
    :rtype: List[List[int]]
    """
    
    if len(nums) <= 1:
        return [nums]
        
    r = []
    for i in range(len(nums)):
        n = nums[:i] + nums[i+1:]
        for y in permute(n):
            r.append([nums[i]]+y)
    return r





nums =  [1,2,3]
permute(nums)
def permute(nums):
    
    if len(nums)<=1:
        return [nums]

    r = []
    for i in range(len(nums)):
        n = nums[:i] + nums[i+1:]

        for y in permute(n):
            r.append([nums[i]]+y)
    return r

11.组合

输入: nums = [1,2,3]
输出:
[
  [3],
  [1],
  [2],
  [1,2,3],
  [1,3],
  [2,3],
  [1,2],
  []
]

nums = [1,2,3]
r = []
dfs(nums,0,[],r)



def dfs(nums,start ,path, r):
    r.append(path[:])
    
    for i in range(start,len(nums)):
        path.append(nums[i])
        dfs(nums,i+1,path,r)
        path.pop()
    

#快速排序
def partition(nums,left,right):

    pivot = nums[left]
    i = left
    j = i + 1
    
    while j<right:
        if pivot > nums[j]:
            i += 1
            nums[i],nums[j] = nums[j],nums[i]
        j += 1
 

    nums[left],nums[i] = nums[i],nums[left]
    return i
    


def quickSort(nums,left,right):
    
    if left>=right:
        return

    index = partition(nums,left,right)
    quickSort(nums,index+1,right)
    quickSort(nums,left,index-1)

nums = [1,2,3,5]
quickSort(nums,0,len(nums)-1)
nums


#冒泡排序

nums = [1,2,6,5,3,8]
bubbleSort(nums)
def bubbleSort(nums):
    n = len(nums)
    for i in range(n-1):
        for j in range(n-i-1):
            if nums[j] > nums[j + 1]:
                nums[j], nums[j + 1] = nums[j + 1], nums[j]
    return nums
             




a = [1,2,3,4]

for i in range(2):
    a.insert(0,a.pop())

#快速排序

nums = [1,2,6,5,3,8]
quickSort(nums,0,len(nums)-1)
def partition(nums,left,right):
    pivot = nums[left]
    i = left
    j = i + 1
    while j<=right:
        if pivot > nums[j]:
            i+=1
            nums[i],nums[j] = nums[j],nums[i]
        j += 1 

    nums[i],nums[left] = nums[left],nums[i]
    return i

def quickSort(nums,left,right):

    if left>=right:
        return
    
    index = partition(nums,left,right)
    quickSort(nums,left,index-1)
    quickSort(nums,index+1,right)
    
    
10. 字符串匹配
#https://www.bilibili.com/video/BV13441117i4?from=search&seid=3027627926171622370




# -*- coding:utf-8 -*-
二分查找
限定语言：Python、C++、C#、Java
对于一个有序数组，我们通常采用二分查找的方式来定位某一元素，请编写二分查找的算法，在数组中查找指定元素。
给定一个整数数组A及它的大小n，同时给定要查找的元素val，请返回它在数组中的位置(从0开始)，若不存在该元素，返回-1。若该元素出现多次，请返回第一次出现的位置。
测试样例：
[1,3,5,7,9],5,3


class BinarySearch:
    def getPos(self, A, n, val):
        # write code here
        left = 0
        right = n - 1
        last_mid = -1
        while left<=right:
            mid = int(left+right) / 2
            
            if A[mid] == val:
                last_mid = mid 
                right = mid - 1

            if val < A[mid]:
                #在有序列表的左边
                right = mid - 1
            elif val > A[mid]:
                #在有序列表的右边
                left = mid + 1

        if last_mid >= 0:
            return last_mid
        
        return -1
      
