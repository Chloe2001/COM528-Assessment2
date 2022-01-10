<%-- 
    Document   : ModifyItem
    Created on : 10 Jan 2022, 10:57:05
    Author     : chase
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<main role="main" class="container">
  
    
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
                    aria-hidden="true">&times;</span>
            </button>
                ${errorMessage}
        </div>
    </c:if>
 

    <c:if test="${not empty message}">
        <div class="alert alert-success alert-dismissible" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
                    aria-hidden="true">&times;</span>
            </button>
                ${message}
        </div>
    </c:if>
    <div>
        <H1>Modify Item: ${modifyItem.name} </H1>

        <form action="./viewModifyItem" method="POST" enctype="multipart/form-data">
            <table class="table">
               
                <tbody>
                <tr>
                    <td>Item ID</td>
                    <td>${modifyItem.id}</td>
                </tr>
                <tr>
                    <td>Item Name</td>
                    <td><input type="text" name="name" value="${modifyItem.name}"/></td>
                </tr>
                <tr>
                    <td>Item Price</td>
                    <td><input type="text" name="price" value="${modifyItem.price}"/></td>
                </tr>
                <tr>
                    <td>Item Quantity</td>
                    <td><input type="text" name="quantity" value="${modifyItem.quantity}"/></td>
                </tr>
                </tbody>

            </table>

            <input type="hidden" name="id" value="${modifyItem.id}"/>
            <input type="hidden" name="action" value="update"/>
            <button class="btn" type="submit">Update Item "${modifyItem.name}"</button>
        </form>

        <form action="./catalog">
            <button class="btn" type="submit">Return To Catalog</button>
        </form>

    </div>

</main>

<jsp:include page="footer.jsp"/>
