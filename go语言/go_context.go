package main

import (
	"context"
	"fmt"
	"time"
)

//https://www.jianshu.com/p/4e61ed8e140a

func init() {
	fmt.Println("init")
}

type A struct {
	xxx int
}

func servicesTimeOut() {

	defer func() {
		if err := recover(); err != nil {
			fmt.Printf("Work failed with %s", err)
		}
	}()

	ctx, cancel := context.WithTimeout(context.Background(), 3*time.Second)
	defer cancel()
	done := make(chan int, 1)

	go func() {
		time.Sleep(5 * time.Second)
		done <- 1
	}()

	select {
	case <-done:
		fmt.Println("done")
	case v, closed := <-ctx.Done():
		fmt.Println("timeout", v, closed)
		close(done)
	}

	time.Sleep(10 * time.Second)

}

func main() {

	servicesTimeOut()

	// ctx, cancel := context.WithTimeout(context.Background(), 3*time.Second)
	// defer cancel()

	// done := make(chan struct{}, 1)

	// go func() {
	// 	time.Sleep(10 * time.Second)
	// 	done <- struct{}{}
	// }()

	// select {
	// case <-done:
	// 	fmt.Println("donw")
	// 	break
	// case v, closed := <-ctx.Done():
	// 	fmt.Println("timeout", v, closed)
	// 	close(done)
	// }

	// time.Sleep(10 * time.Second)

}
