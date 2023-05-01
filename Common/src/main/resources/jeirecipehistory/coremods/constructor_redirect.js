var Opcodes = Java.type('org.objectweb.asm.Opcodes');

function transConstructToFactory(method, oldOwner, newOwner, name, desc) {
    var instructions = method.instructions.toArray();
    for (var i = 0, n; i < instructions.length; i++) {
        n = instructions[i];
        if (n.getOpcode() === Opcodes.NEW && n.desc === oldOwner) {
        method.instructions.remove(n.getNext()); //remove insn dup
        method.instructions.remove(n); //remove type insn
        } else if (n.getOpcode() === Opcodes.INVOKESPECIAL && n.owner === oldOwner) {
            n.setOpcode(Opcodes.INVOKESTATIC);
            n.owner = newOwner;
            n.name = name;
            n.desc = desc;
        }
    }
}

function transConstructWithOrdinal(method, oldOwner, newOwner, name, desc, ordinal) {
  var instructions = method.instructions.toArray();
  var count = -1;
  for (var i = 0, n; i < instructions.length; i++) {
    n = instructions[i];
    if (n.getOpcode() === Opcodes.NEW && n.desc === oldOwner) {
      count++;
      if (count === ordinal) {
        method.instructions.remove(n.getNext()); //remove insn dup
        method.instructions.remove(n); //remove type insn
      }
    } else if (n.getOpcode() === Opcodes.INVOKESPECIAL && n.owner === oldOwner) {
      if (count === ordinal) {
        n.setOpcode(Opcodes.INVOKESTATIC);
        n.owner = newOwner;
        n.name = name;
        n.desc = desc;
      }
    }
  }
}

var FOCUS_HANDLER_OLD_OWNER = 'mezz/jei/input/mouse/handlers/FocusInputHandler';
var FOCUS_HANDLER_NEW_OWNER = 'com/christofmeg/jeirecipehistory/gui/input/handler/ExtendedFocusInputHandler';
var FOCUS_HANDLER_METHOD_NAME = 'create';
var FOCUS_HANDLER_METHOD_DESC = '(Lmezz/jei/input/CombinedRecipeFocusSource;Lmezz/jei/gui/recipes/RecipesGui;)Lmezz/jei/input/mouse/IUserInputHandler;';

var INGREDIENT_GRID_OLD_OWNER = 'mezz/jei/gui/overlay/IngredientGrid';
var INGREDIENT_GRID_NEW_OWNER = 'com/christofmeg/jeirecipehistory/gui/history/AdvancedIngredientListGrid';
var INGREDIENT_GRID_METHOD_NAME = 'create';
var INGREDIENT_GRID_METHOD_DESC = '(Lmezz/jei/ingredients/RegisteredIngredients;Lmezz/jei/config/IIngredientGridConfig;Lmezz/jei/config/IEditModeConfig;Lmezz/jei/config/IIngredientFilterConfig;Lmezz/jei/core/config/IClientConfig;Lmezz/jei/core/config/IWorldConfig;Lmezz/jei/gui/GuiScreenHelper;Lmezz/jei/api/helpers/IModIdHelper;Lmezz/jei/common/network/IConnectionToServer;)Lmezz/jei/gui/overlay/IngredientGrid;';

function initializeCoreMod() {
    return {
        'transFocusInputHandler': {
            'target': {
                'type': 'METHOD',
                'class': 'mezz.jei.startup.JeiStarter',
                'methodName': 'start',
                'methodDesc': '(Lmezz/jei/forge/events/RuntimeEventSubscriptions;)V'
            }, 'transformer': function (method) {
                transConstructToFactory(method,
                    FOCUS_HANDLER_OLD_OWNER,
                    FOCUS_HANDLER_NEW_OWNER,
                    FOCUS_HANDLER_METHOD_NAME,
                    FOCUS_HANDLER_METHOD_DESC
                )
                return method;
            }
        },
        'transIngredientListGrid': {
            'target': {
                'type': 'METHOD',
                'class': 'mezz.jei.startup.JeiStarter',
                'methodName': 'start',
                'methodDesc': '(Lmezz/jei/forge/events/RuntimeEventSubscriptions;)V'
            }, 'transformer': function (method) {
                transConstructWithOrdinal(method,
                    INGREDIENT_GRID_OLD_OWNER,
                    INGREDIENT_GRID_NEW_OWNER,
                    INGREDIENT_GRID_METHOD_NAME,
                    INGREDIENT_GRID_METHOD_DESC,
                    0
                )
                return method;
            }
        },
        'transBookmarkOverlay': {
                    'target': {
                        'type': 'METHOD',
                        'class': 'mezz.jei.startup.JeiStarter',
                        'methodName': 'start',
                        'methodDesc': '(Lmezz/jei/forge/events/RuntimeEventSubscriptions;)V'
                    }, 'transformer': function (method) {
                        transConstructToFactory(method,
                            'mezz/jei/gui/overlay/bookmarks/BookmarkOverlay',
                            'com/christofmeg/jeirecipehistory/config/AdvancedBookmarkOverlay',
                            'create',
                            '(Lmezz/jei/bookmarks/BookmarkList;Lmezz/jei/gui/textures/Textures;Lmezz/jei/gui/overlay/IngredientGridWithNavigation;Lmezz/jei/core/config/IClientConfig;Lmezz/jei/core/config/IWorldConfig;Lmezz/jei/gui/GuiScreenHelper;Lmezz/jei/common/network/IConnectionToServer;)Lmezz/jei/gui/overlay/bookmarks/BookmarkOverlay;'
                        )
                        return method;
                    }
                }
    }
}