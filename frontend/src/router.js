
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);


import OrderManager from "./components/OrderManager"

import MyPage from "./components/MyPage"
import Menu from "./components/Menu"
import PayManager from "./components/PayManager"

import ProductManager from "./components/ProductManager"

import DeliveryManager from "./components/DeliveryManager"

export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
            {
                path: '/Order',
                name: 'OrderManager',
                component: OrderManager
            },

            {
                path: '/MyPage',
                name: 'MyPage',
                component: MyPage
            },
            {
                path: '/Menu',
                name: 'Menu',
                component: Menu
            },
            {
                path: '/Pay',
                name: 'PayManager',
                component: PayManager
            },

            {
                path: '/Product',
                name: 'ProductManager',
                component: ProductManager
            },

            {
                path: '/Delivery',
                name: 'DeliveryManager',
                component: DeliveryManager
            },



    ]
})
