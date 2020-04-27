package main

import (
	"fmt"
	"runtime"
	"strconv"
)

type A struct {
	a1 int
	a2 int
}

func (a *A) addSum() (x int) {
	x = a.a1 + a.a2
	return x
}

type B struct {
	A
	b1 int
}

type Square struct {
	area int
}

type Circle struct {
	radius int
}

type Shaper interface {
	Area() int
}

func (s *Square) Area() int {
	return s.area * s.area
}
func (c *Circle) Area() int {
	return c.radius * 2 * 3
}

func main() {

	s := &Square{1}
	c := &Circle{1}
	shapers := []Shaper{s, c}

	for _, shape := range shapers {
		fmt.Printf("area is %d\r\n", shape.Area())
	}

	b := B{A{1, 3}, 1}
	sa := A{1, 3}
	fmt.Printf("%v\r\n", b)
	res := sa.addSum()
	fmt.Printf("The sum is: %d\n", res)

	where := func() {
		_, file, line, _ := runtime.Caller(1)
		fmt.Printf("%s:%d", file, line)
	}

	var i int = 5
	fmt.Printf("i is %d , %p\r\n", i, &i)
	var ip *int
	ip = &i
	fmt.Printf("ip is %p, %d\r\n", ip, *ip)

	d, err := strconv.Atoi("234d")
	if err == nil {
		fmt.Printf("d is %d\r\n", d)
	} else {
		fmt.Printf("%s\r\n", err)
	}

	where()

	for i := 0; i < 5; i++ {
		fmt.Println(i)
	}

	for i, j := 0, 5; i < 5; i, j = i+1, j-1 {
		fmt.Printf("%d,%d\n", i, j)
	}

	str := "Go is a beautiful language!"

	for i := 0; i < len(str); i++ {
		fmt.Printf("%c", str[i])
	}
	fmt.Println()

	for i, char := range str {
		fmt.Printf("%d,%c", i, char)
	}
	fmt.Println()

	x := sum(1, 2, 3)
	fmt.Println(x)

	var r int
	reply(1, 2, &r)
	fmt.Println(r)
	rundefer()
	fab(10)

	s3 := []int{1, 2, 3}
	s3 = append(s3, 4, 5, 6)
	fmt.Println(s3)

	m := map[string]int{"a": 1, "b": 2}
	for k, v := range m {
		fmt.Printf("%s:%d\r\n", k, v)
	}
	var m2 map[string]int
	m2 = make(map[string]int)
	m2["s"] = 1
	m2["t"] = 2

	for k, v := range m2 {
		fmt.Println(k, v)
	}

	m3 := map[string]int{}
	m3["s"] = 3
	m3["m"] = 4
	for k, v := range m3 {
		fmt.Println(k, v)
	}

	//mergeSort()

}

func sum(x int, y int, z int) int {
	return x + y + z
}

func reply(x int, y int, r *int) {

	*r = x + y
}

func rundefer() {
	i := 0
	defer fmt.Println(i)
	i++
}

func fab(n int) {
	x, y := 1, 1
	for i := 0; i < n; i++ {
		x, y = y, x+y
	}
	fmt.Println(x)
}

// func mergeSort() {
// 	a := [3][3]int{{1, 2, 4}, {3, 4, 5}, {5, 6, 7}}

// 	k := len(a)

// 	for {
// 		if k <= 1 {
// 			break
// 		}

// 		r := []int{}
// 		for i := 0; i < k/2; i++ {
// 			var l1 [3]int = a[i]
// 			var l2 [3]int = a[(k+1)/2+i]
// 			merge(l1, l2, &r)
// 		}
// 	}

// }

// func merge(l1 [3]int, l2 [3]int, r *[]int) {

// 	if len(l1) == 0 {
// 		for i, v := range l2 {
// 			append(*r, v)
// 		}
// 	}

// 	if len(l2) == 0 {
// 		for i, v := range l1 {
// 			append(*r, v)
// 		}
// 	}

// 	if l1[0] <= l2[0] {
// 		append(*r, l1)
// 		merge(l1[1:len(l1)], l2, r)
// 	} else {
// 		append(*r, l1)
// 		merge(l1[1:len(l1)], l2, r)
// 	}

// }
