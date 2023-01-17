package com.ederfmatos.spring.exception

class OrderNotFoundException : RuntimeException("Pedido não encontrado")
class OrderAlreadyCancelledException : RuntimeException("Pedido já cancelado")
class OrderAlreadyFinishedException : RuntimeException("Pedido já finalizado")
