mutation {
<% products.eachWithIndex { product, i -> %>
  p${i}: productCreate(input: {
    title: "${product.title}"
    descriptionHtml: "${product.description}"
    productType: "Software"
    tags: [<% product.tags.each { out << "\"${it}\"," } %>]
  }) {
    product {
      id
      title
    }
    userErrors {
      field
      message
    }
  }
<% } %>
}
