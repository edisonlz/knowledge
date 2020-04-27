package main

import (
	"fmt"
	"sync"
	"time"
	"github.com/panjf2000/ants"
)

//go go-coding-in-go-way
//https://taohuawu.club/high-performance-implementation-of-goroutine-pool
//https://tonybai.com/2017/04/20/go-coding-in-go-way/?hmsr=toutiao.io&utm_medium=toutiao.io&utm_source=toutiao.io
//https://www.lagou.com/lgeduarticle/110887.html

func add(i interface{}) {
	fmt.Println(i)
	time.Sleep(time.Second * 5)
}

func main() {

	wg := &sync.WaitGroup{}

	p, _ := ants.NewUltimatePoolWithFunc(10, 1, func(i interface{}) {
		add(i)
		wg.Done()
	}, true)
	defer p.Release()

	for i := 0; i < 20; i++ {
		wg.Add(1)
		p.Invoke(i)
	}
	wg.Wait()

}
