# encoding: utf-8
'''Easy Switch Server Mock'''

import web

urls = (
    '/listItems',  'listItems',
    '/register',   'register',
    '/unregister', 'unregister',
    '/collect',    'collect',
)

class listItems:
    def GET(self):
        return '[{"name":"1","on":true,"threshold":1},{"name":"2","on":false,"threshold":2},{"name":"3","on":true,"threshold":3}]'

class register:
    def GET(self):
        return '{"result" : true}'

class unregister:
    def GET(self):
        return '{"result" : true}'

class collect:
    def GET(self):
        return '{"result" : true, "message" : "Fails [items.1, items.2]"}'

if __name__ == "__main__":
    app = web.application(urls, globals())
    app.run()

