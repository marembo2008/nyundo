# nyundo
a tag namespace aware rewrite and renderer
Main logic is how to handle html components.

Instead of generating html per components, we define component templates and then use simple naming to manage the html components.

Consider we hve the following:

{code}
/src
    /main
        /resources
            /html-components
                next-button.html
{code}
Then from your main html file

---
/src
    /main
        /webapp
            steps.html
            <html>
              <head><head>
              <body>
                <nextButton name="Next" action="what do i do"/>
              </body>
            </html>
---
