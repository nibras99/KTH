<?php

namespace App;

class Recipe
{
    public $data;

    function __construct($recipe)
    {
        $xmlData = simplexml_load_file(resource_path("assets/xml/cookbook.xml"));
        foreach ($xmlData->recipe as $item)
        {
            if ($item->title == $recipe)
            {
                $this->data = $item;
                break;
            }
        }
    }
}
