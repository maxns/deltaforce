package org.namstorm.deltaforce.ledgers;

/**
 * Created by maxnamstorm on 8/8/2016.
 *
 * The idea is simple:
 * Why:
 *      to enable delta-ledgering of related objects
 * What:
 *      a ledger - to create ledger entries to correspond with an atomic change across several objects
 *          - pre-wired to understand what kind of objects will change together
 *          - knows how to work with delta builders to capture deltas
 *
 *
 *      a ledger entry - an entry that records deltas of all related objects that participated in the change
 *          - optimised to be storage-efficient
 *
 *      a ledger reader - to read ledger entries and apply them to recreate objects upto any point
 *
 * How:
 *      You create a ledger schema, by creating a class with fields that correspond to related objects
 *          Note:
 *              if you haven't already annotated classes with @DeltaBuilder of their own, then you can do it here,
 *              of'course this means you don't have as much control
 *
 *
 *      IE:
 *      <code>
 *          class OrderLedgerSchema {
 *              @DeltaBuilder
 *              Order order;
 *              @DeltaBuilder
 *              Status status;
 *              @DeltaBuilder
 *              List<Event> events;
 *
 *          }
 *
 *      </code>
 *
 *
 *      Annotate it with @DeltaLedger
 *      <code>
 *          @DeltaLedger
 *          class OrderLedgerSchema...
 *      </code>
 *
 *      This, in turn, generates a OrderLedger object with builders methods that correspond to an action and return builders for a particular object, ie:
 *      ie:
 *          createXXX - if you're creating a new object
 *          editXXX - if you're updating something
 *          deleteXXX - if you're deleting
 *
 *      in addition, it'll generate special special builders for Maps, Sets and Lists
 *

 *      <code>
 *          @Generated
 *          class OrderLedger {
 *              OrderBuilder updateOrder();
 *              StatusBuilder updateStatus();
 *              ListBuilder<Event>< updateEvents();
 *          }
 *      </code>
 *
 */
public class AbstractLedger {
}
