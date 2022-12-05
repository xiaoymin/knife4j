<template>
  <div>
    <span v-if="!record.validateStatus">{{text==null||text==''?'string':text}}</span>
    <span v-else class="knife4j-request-validate-jsr">
      <a-tooltip placement="right">
        <template slot="title">
          <div v-for="pt in validators" :key="pt.key">{{pt.val}}</div>
        </template>
        {{text}}
      </a-tooltip>
    </span>

  </div>
</template>
<script>
export default {
  name: "DataType",
  props: {
    text: {
      type: String,
      default:"string",
      required: true
    },
    record: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      validators: []
    };
  },
  created() {
    this.intiValidator();
  },
  methods: {
    intiValidator() {
      var that = this;
      const record = this.record;
      if (record.validateInstance != null) {
        var len = that.getJsonKeyLength(record.validateInstance);
        var _size = 0;
        for (var k in record.validateInstance) {
          var str = k + ":" + record.validateInstance[k];
          that.validators.push({ key: k, val: str });
        }
      }
    },
    getJsonKeyLength(json) {
      var size = 0;
      if (json != null) {
        for (var key in json) {
          if (json.hasOwnProperty(key)) size++;
        }
      }
      return size;
    }
  }
};
</script>