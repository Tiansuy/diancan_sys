import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../layout/Layout.vue'
import ServantLayout from '../layout/ServantLayout.vue'

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect: '/login',
    children: [
      {
        path: '/notice',
        name: 'Notice',
        component: () => import('../views/Notice.vue')
      },
      {
        path: '/show-notice',
        name: 'ShowNotice',
        component: () => import('../views/ShowNotice.vue')
      },
      {
        path: '/user',
        name: 'User',
        component: () => import('../views/User.vue')
      },
      {
        path: '/order',
        name: 'Order',
        component: () => import('../views/Order.vue')
      },
      {
        path: '/food',
        name: 'Food',
        component: () => import('../views/Food.vue')
      }
    ]
  },
  {
    path: '/non-admin',
    name: 'ServantLayout',
    component: ServantLayout,
    redirect: '/show-notice',
    children: [{
      path: '/show-notice',
      name: 'ShowNotice',
      component: () => import('../views/ShowNotice.vue')
    },{
      path: '/show-food',
      name: 'FoodList',
      component: () => import('../views/FoodList.vue')
    },{
      path: '/manage-order',
      name: 'ManageOrder',
      component: () => import('../views/ManageOrder.vue')
    },{
      path: '/show-order',
      name: 'OrderList',
      component: () => import('../views/OrderList.vue')
    }]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import("@/views/Login")
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router