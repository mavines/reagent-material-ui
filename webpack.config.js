const path = require('path')
const { merge } = require('webpack-merge')

const common = {
  output: {
    filename: '[name].inc.js',
    path: path.resolve(__dirname, 'src/generated/material-ui')
  },
  externals: [{
    'react': 'React',
    'react-dom': 'ReactDOM'
  }],
  devtool: false
}

const production = {
  output: {
    filename: '[name].min.inc.js'
  }
}

const externals = [
  function(context, request, callback) {
    const re = /^@material-ui\/core\/(.*)$/
    if (re.test(request)) {
      const [_, name] = re.exec(request)
      if (name === 'utils') {
        return callback()
      }
      const external = name === 'styles' ? 'MaterialUIStyles' : `MaterialUI.${name}`
      return callback(null, `root ${external}`)
    }
    callback()
  },
  {
    '@material-ui/core': 'MaterialUI',
    '@material-ui/styles': 'MaterialUIStyles'
  }
]

const materialUI = {
  entry: {
    'material-ui': './src/js/material-ui.js'
  }
}

const materialUIPickers = {
  entry: {
    'material-ui-pickers': './src/js/material-ui-pickers.js'
  },
  externals
}

const materialUILab = {
  entry: {
    'material-ui-lab': './src/js/material-ui-lab.js'
  },
  externals
}

module.exports = function (env, argv) {
  const base = argv.mode === 'production' ? merge(common, production) : common
  return [
    merge(base, materialUI),
    merge(base, materialUIPickers),
    merge(base, materialUILab)
  ]
}
