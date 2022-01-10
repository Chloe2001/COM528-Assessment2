<%-- 
    Document   : content
    Created on : Jan 4, 2020, 11:19:47 AM
    Author     : cgallen
--%>

<%@page import="org.solent.com504.oodd.cart.model.dto.ShoppingItem"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
// request set in controller
//    request.setAttribute("selectedPage","about");
%>
<jsp:include page="header.jsp" />
<!-- Begin page content -->
<main role="main" class="container">
    <H1>Cataloge</H1>
    
    <H1>Available Items</H1>
    <a href="./viewCreateItem"><p>Create Item</p></a>
  
    <table class="table">

        <tr>
            <th>Item Name</th>
            <th>Price</th>
            <th></th>
        </tr>

        <c:forEach var="item" items="${availableItems}">

            <tr>
                <td>${item.name}</td>
                <td>${item.price}</td>
                <td></td>
                <td>
                    <!-- post avoids url encoded parameters -->
                    <form action="./home" method="get">
                        <input type="hidden" name="itemName" value="${item.name}">
                        <input type="hidden" name="action" value="addItemToCart">
                        <button type="submit" >Add Item</button>
                    </form> 
                </td>
            </tr>
            
        </c:forEach>
   

            <tr>
                <th>Item Name</th>
                <th>Price</th>
                <th>Quantity</th>
            </tr>
            <c:forEach var="item" items="${shoppingCartItems}">
                <tr>
                    <td>${item.name}</td>
                     <td>${item.price}</td>
                <td>${item.quantity}</td>
                <td>
                    <!-- post avoids url encoded parameters -->
                    <form action="./cart" method="post">
                        <input type="hidden" name="itemUUID" value="${item.uuid}">
                        <input type="hidden" name="itemName" value="${item.name}">
                        <input type="hidden" name="action" value="removeItemFromCart">
                        <button type="submit" >Remove Item</button>
                    </form>
                </td>
            </tr>
            </c:forEach>
            <tr>
                <td>Total</td>
                <td>${shoppingcartTotal}</td>
            </tr>
            
         </table>
    
    
</main>




<jsp:include page="footer.jsp" />
