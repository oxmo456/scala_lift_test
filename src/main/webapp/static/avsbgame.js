var AvsBGameCell = function(aView, bView, disabledIndicator) {
    var container = new Container();
    var enabled = true;

    container.addChild(aView);
    container.addChild(bView);
    container.addChild(disabledIndicator);
    disabledIndicator.visible = false;
    disabledIndicator.alpha = 0.5;

    this.getContainer = function() {
        return container;
    };

    this.showA = function() {
        aView.visible = true;
        bView.visible = false;
    };

    this.showB = function() {
        aView.visible = false;
        bView.visible = true;
    };

    this.setViewAAlpha = function(value) {
        aView.alpha = value;
    };

    this.setViewBAlpha = function(value) {
        bView.alpha = value;
    };

    this.disable = function() {
        enabled = false;
        disabledIndicator.visible = true;
    };

    this.enable = function() {
        enabled = true;
        disabledIndicator.visible = false;
    };

    this.isEnabled = function() {
        return enabled;
    };

};

var AvsBGame = function(spriteSheetURL, spriteSheetWidth, spriteSheetHeight, data, cellClickHandler, playerSide) {
    var SELF = this;
    var OPONNENT_ALPHA = 0.1;
    var SIDE_A = 1;
    var SIDE_B = 0;
    var NO_SIDE = -1;
    var gridSize;

    var playerSide;
    var spriteSheet;
    var cells = [];
    var gridColumns;
    var gridRows;
    var cellSelectionIndicator;
    var canvas;
    var stage;

    function buildCellSelectionIndicator(thickness) {
        var g = new Graphics();
        g.beginFill(Graphics.getRGB(0, 0, 0));
        g.drawRect(0, 0, gridSize, thickness);
        g.drawRect(0, gridSize - thickness, gridSize, thickness);
        g.drawRect(0, thickness, thickness, gridSize - thickness * 2);
        g.drawRect(gridSize - thickness, thickness, thickness, gridSize - thickness * 2);
        cellSelectionIndicator = new Shape(g);
        stage.addChild(cellSelectionIndicator);
    }

    function buildDisabledCellIndicator() {
        var g = new Graphics();
        g.beginFill(Graphics.getRGB(0, 0, 0));
        g.drawRect(0, 0, gridSize, gridSize);
        return new Shape(g);
    }


    function loadImage(imageURL, callback) {
        var image = new Image();

        function imageLoadingComplete() {
            callback.call(null, image);
        }

        image.onload = imageLoadingComplete;
        image.src = imageURL;
    }

    function createSpriteSheet(image, frameWidth, frameHeight, animations) {
        var spriteSheet = new SpriteSheet({
            images: [image],
            frames: {
                width: frameWidth,
                height: frameHeight
            },
            animations: animations
        });
        return spriteSheet;
    }

    function buildGrid() {
        var spriteSheetColumns = spriteSheetWidth / gridSize;
        gridColumns = (spriteSheetWidth * 0.5) / gridSize;
        gridRows = spriteSheetHeight / gridSize;
        for (var y = 0; y < gridRows; y++) {
            for (var x = 0; x < gridColumns; x++) {
                var viewA = new Bitmap(SpriteSheetUtils.extractFrame(spriteSheet, y * spriteSheetColumns + (x + gridColumns)));
                var viewB = new Bitmap(SpriteSheetUtils.extractFrame(spriteSheet, y * spriteSheetColumns + x));
                var gridCell = new AvsBGameCell(viewA, viewB, buildDisabledCellIndicator());
                var index = cells.push(gridCell);
                if (data[--index] == SIDE_A) {
                    gridCell.showA();
                } else {
                    gridCell.showB();
                }
                gridCell.getContainer().x = x * gridSize;
                gridCell.getContainer().y = y * gridSize;
                stage.addChildAt(gridCell.getContainer(), 0);
            }
        }
    }

    function stageOnClick() {
        var px = Math.floor(stage.mouseX / gridSize);
        var py = Math.floor(stage.mouseY / gridSize);
        var cellIndex = py * gridColumns + px;
        var cell = cells[cellIndex];
        if (data[cellIndex] !== playerSide && cell.isEnabled()) {
            cell.disable();
            cellClickHandler(cellIndex, playerSide);
        }
    }

    function updateCellsAlpha() {
        var cellCount = cells.length;
        for (var i = 0; i < cellCount; i++) {
            var cell = cells[i];
            if (playerSide === SIDE_A) {
                cell.setViewBAlpha(OPONNENT_ALPHA);
            } else if (playerSide === SIDE_B) {
                cell.setViewAAlpha(OPONNENT_ALPHA);
            } else {
                cell.setViewAAlpha(1);
                cell.setViewBAlpha(1);
            }
        }
    }

    function stageOnMouseMove() {
        var px = Math.floor(stage.mouseX / gridSize);
        var py = Math.floor(stage.mouseY / gridSize);
        cellSelectionIndicator.x = px * gridSize;
        cellSelectionIndicator.y = py * gridSize;
    }

    this.setPlayerSide = function(value) {
        if (value !== NO_SIDE) {
            playerSide = value;
            updateCellsAlpha();
            stage.onClick = stageOnClick;
            stage.onMouseMove = stageOnMouseMove;
            buildCellSelectionIndicator(4);
        }
    };

    this.updateCell = function(cellIndex, value) {
        data[cellIndex] = value;
        var cell = cells[cellIndex];
        if (value === SIDE_A) {
            cell.showA();
        } else {
            cell.showB();
        }
        cell.enable();
    };


    this.tick = function() {
        stage.update();
    };


    canvas = document.getElementById("canvas");
    stage = new Stage(canvas);

    gridSize = spriteSheetHeight / Math.sqrt(data.length);


    loadImage(spriteSheetURL,
        function(image) {
            spriteSheet = createSpriteSheet(image, gridSize, gridSize, null);
            buildGrid();
            SELF.setPlayerSide(playerSide);
            Ticker.setFPS(24);
            Ticker.addListener(SELF);
        });


};
